package com.mall.marketing.coupon.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mall.common.domain.dto.PageDTO;
import com.mall.common.exceptions.BadRequestException;
import com.mall.common.utils.BeanUtils;
import com.mall.common.utils.CollUtils;
import com.mall.common.utils.UserContext;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * @author lm
 * @description 针对表【user_coupon(用户领取优惠券的记录，是真正使用的优惠券信息)】的数据库操作Service实现
 * @createDate 2024-06-06 15:54:53
 */
@Service
@AllArgsConstructor
public class UserCouponServiceImpl implements UserCouponService {

    private final CouponMapper couponMapper;
    private final UserCouponMapper userCouponMapper;

    @Override
    @Transactional
    public void receiveCoupon(Long id, Long userId) {

        Coupon coupon = couponMapper.selectById(id);
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

        synchronized (userId.toString().intern()) {
            LambdaQueryWrapper<UserCoupon> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(UserCoupon::getCouponId, id)
                    .eq(UserCoupon::getUserId, userId);
            List<UserCoupon> userCoupons = userCouponMapper.selectList(queryWrapper);
            if (userCoupons != null && userCoupons.size() >= coupon.getUserLimit()) {
                throw new BadRequestException("已领取优惠券!");
            }

            boolean issueSuccess = couponMapper.incrIssueNum(coupon.getId());

            if (issueSuccess) {
                UserCoupon userCoupon = new UserCoupon();
                userCoupon.setCouponId(coupon.getId());
                //userCoupon.setUserId(UserContext.getUserId());
                userCoupon.setUserId(userId);
                userCoupon.setStatus(UserCouponStatus.UNUSED);
                userCoupon.setTermBeginTime(coupon.getTermBeginTime());
                userCoupon.setTermEndTime(coupon.getTermEndTime());
                userCouponMapper.insert(userCoupon);
            }
        }
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




