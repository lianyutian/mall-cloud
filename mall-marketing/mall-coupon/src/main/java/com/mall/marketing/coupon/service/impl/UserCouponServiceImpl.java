package com.mall.marketing.coupon.service.impl;

import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mall.common.autoconfigure.mq.RocketMQEnhanceTemplate;
import com.mall.common.autoconfigure.redisson.annotations.Lock;
import com.mall.common.domain.dto.PageDTO;
import com.mall.common.exceptions.BadRequestException;
import com.mall.common.exceptions.BizIllegalException;
import com.mall.common.utils.BeanUtils;
import com.mall.common.utils.CollUtils;
import com.mall.common.utils.UserContext;
import com.mall.marketing.coupon.constants.CouponConstants;
import com.mall.marketing.coupon.domain.message.UserCouponMessage;
import com.mall.marketing.coupon.domain.po.Coupon;
import com.mall.marketing.coupon.domain.po.UserCoupon;
import com.mall.marketing.coupon.domain.query.UserCouponQuery;
import com.mall.marketing.coupon.domain.vo.CouponVO;
import com.mall.marketing.coupon.enums.CouponStatus;
import com.mall.marketing.coupon.enums.ObtainWay;
import com.mall.marketing.coupon.enums.UserCouponStatus;
import com.mall.marketing.coupon.mapper.CouponMapper;
import com.mall.marketing.coupon.mapper.UserCouponMapper;
import com.mall.marketing.coupon.service.UserCouponService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author lm
 * @description 针对表【user_coupon(用户领取优惠券的记录，是真正使用的优惠券信息)】的数据库操作Service实现
 * @createDate 2024-06-06 15:54:53
 */
@Service
@AllArgsConstructor
@EnableAspectJAutoProxy(exposeProxy = true)
public class UserCouponServiceImpl implements UserCouponService {

    private final CouponMapper couponMapper;
    private final UserCouponMapper userCouponMapper;
    private final RocketMQEnhanceTemplate rocketMQEnhanceTemplate;
    private final StringRedisTemplate redisTemplate;

    @Override
    @Lock(name = "lock:coupon:#{id}")
    public void receiveCoupon(Long id, Long userId) {
        Coupon coupon = queryCouponByCache(id);

        if (coupon == null) {
            throw new BadRequestException("优惠券不存在!");
        }
        if (coupon.getStatus() != CouponStatus.ISSUING) {
            throw new BadRequestException("优惠券未发放!");
        }
        if (coupon.getObtainWay() != ObtainWay.PUBLIC) {
            throw new BadRequestException("优惠券不可领取!");
        }
        LocalDateTime now = LocalDateTime.now();
        if (coupon.getIssueBeginTime().isAfter(now) || coupon.getIssueEndTime().isBefore(now)) {
            throw new BadRequestException("优惠券发放已经结束或尚未开始!");
        }
        if (Objects.equals(coupon.getIssueNum(), coupon.getTotalNum())) {
            throw new BadRequestException("优惠券已发完!");
        }

        // 5.扣减优惠券库存
        // 如果刚扣减完库存宕机了，此时库存会多余
        redisTemplate.opsForHash().increment(
                CouponConstants.COUPON_CACHE_KEY_PREFIX + id, "totalNum", -1);

        // 发送MQ消息
        UserCouponMessage userCouponMessage = new UserCouponMessage();
        rocketMQEnhanceTemplate.send("MarketingCoupons", userCouponMessage);
    }

    private Coupon queryCouponByCache(Long id) {
        // 1.准备KEY
        String key = CouponConstants.COUPON_CACHE_KEY_PREFIX + id;
        // 2.查询
        Map<Object, Object> objMap = redisTemplate.opsForHash().entries(key);
        if (objMap.isEmpty()) {
            return null;
        }
        // 3.数据反序列化
        return BeanUtils.toBean(objMap, Coupon.class, CopyOptions.create());
    }

    // 这里进事务，同时，事务方法一定要public修饰
    @Transactional
    @Override
    public void checkAndCreateUserCoupon(UserCouponMessage userCouponMessage) {
        Coupon coupon = couponMapper.selectById(userCouponMessage.getCouponId());
        if (coupon == null) {
            throw new BizIllegalException("优惠券不存在！");
        }
        int num = couponMapper.incrIssueNum(coupon.getId());
        if (num == 0) {
            throw new BizIllegalException("优惠券库存不足！");
        }

        // 3.新增一个用户券
        UserCoupon userCoupon = new UserCoupon();
        userCoupon.setCouponId(coupon.getId());
        userCoupon.setUserId(userCouponMessage.getUserId());
        userCoupon.setStatus(UserCouponStatus.UNUSED);
        userCoupon.setTermBeginTime(coupon.getTermBeginTime());
        userCoupon.setTermEndTime(coupon.getTermEndTime());
        userCouponMapper.insert(userCoupon);
    }

    @Override
    public PageDTO<CouponVO> queryUserCoupons(UserCouponQuery userCouponQuery) {
        Integer status = userCouponQuery.getStatus();
        // 1.分页查询
        LambdaQueryWrapper<UserCoupon> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper
                .eq(UserCoupon::getUserId, UserContext.getUserId())
                .eq(status != null, UserCoupon::getStatus, status);

        Page<UserCoupon> userCouponPage = userCouponMapper.selectPage(
                userCouponQuery.toMpPageDefaultSortByCreateTimeDesc(),
                lambdaQueryWrapper);

        List<UserCoupon> userCouponList = userCouponPage.getRecords();
        if (CollUtils.isEmpty(userCouponList)) {
            return PageDTO.empty(userCouponPage);
        }

        List<CouponVO> couponVOList = BeanUtils.copyList(userCouponList, CouponVO.class);
        return PageDTO.of(userCouponPage, couponVOList);
    }
}




