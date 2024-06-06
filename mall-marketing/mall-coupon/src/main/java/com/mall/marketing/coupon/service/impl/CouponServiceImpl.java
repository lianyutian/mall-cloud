package com.mall.marketing.coupon.service.impl;

import com.baomidou.mybatisplus.core.batch.MybatisBatch;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mall.common.domain.dto.PageDTO;
import com.mall.common.exceptions.BadRequestException;
import com.mall.common.exceptions.MallException;
import com.mall.common.utils.BeanUtils;
import com.mall.common.utils.CollUtils;
import com.mall.common.utils.StringUtils;
import com.mall.common.utils.UserContext;
import com.mall.marketing.coupon.domain.dto.CouponFormDTO;
import com.mall.marketing.coupon.domain.dto.CouponIssueFormDTO;
import com.mall.marketing.coupon.domain.po.Coupon;
import com.mall.marketing.coupon.domain.po.UserCoupon;
import com.mall.marketing.coupon.domain.query.CouponQuery;
import com.mall.marketing.coupon.domain.vo.CouponDetailVO;
import com.mall.marketing.coupon.domain.vo.CouponPageVO;
import com.mall.marketing.coupon.domain.vo.CouponVO;
import com.mall.marketing.coupon.enums.CouponStatus;
import com.mall.marketing.coupon.enums.ObtainWay;
import com.mall.marketing.coupon.enums.UserCouponStatus;
import com.mall.marketing.coupon.mapper.CouponMapper;
import com.mall.marketing.coupon.mapper.UserCouponMapper;
import com.mall.marketing.coupon.service.CouponService;
import lombok.AllArgsConstructor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author lm
 * @description 针对表【coupon(优惠券的规则信息)】的数据库操作Service实现
 * @createDate 2024-06-05 15:15:05
 */
@Service
@AllArgsConstructor
public class CouponServiceImpl implements CouponService {

    private final CouponMapper couponMapper;
    private final UserCouponMapper userCouponMapper;
    private final SqlSessionFactory sqlSessionFactory;

    @Override
    @Transactional
    public void saveCoupon(CouponFormDTO couponFormDTO) {
        Coupon coupon = BeanUtils.copyBean(couponFormDTO, Coupon.class);
        couponMapper.insert(coupon);
    }

    @Override
    public PageDTO<CouponPageVO> queryCouponsByPage(CouponQuery query) {
        Integer status = query.getStatus();
        String name = query.getName();
        Integer type = query.getType();
        // 1.分页查询
        LambdaQueryWrapper<Coupon> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper
                .eq(type != null, Coupon::getDiscountType, type)
                .eq(status != null, Coupon::getStatus, status)
                .like(StringUtils.isNotBlank(name), Coupon::getName, name);

        Page<Coupon> couponPage = couponMapper.selectPage(query.toMpPageDefaultSortByCreateTimeDesc(), lambdaQueryWrapper);

        List<Coupon> couponList = couponPage.getRecords();
        if (CollUtils.isEmpty(couponList)) {
            return PageDTO.empty(couponPage);
        }

        List<CouponPageVO> couponPageVOList = BeanUtils.copyList(couponList, CouponPageVO.class);
        return PageDTO.of(couponPage, couponPageVOList);
    }

    @Override
    @Transactional
    public void issueCoupon(CouponIssueFormDTO couponIssueFormDTO) {
        Coupon coupon = couponMapper.selectById(couponIssueFormDTO.getId());
        if (coupon == null) {
            throw new BadRequestException("优惠券不存在!");
        }

        if (coupon.getStatus() != CouponStatus.DRAFT && coupon.getStatus() != CouponStatus.PAUSE) {
            throw new BadRequestException("优惠券状态异常!");
        }

        // 是否立即发放
        LocalDateTime issueBeginTime = couponIssueFormDTO.getIssueBeginTime();
        boolean isBegin = issueBeginTime == null || !issueBeginTime.isBefore(LocalDateTime.now());
        Coupon copyBean = BeanUtils.copyBean(couponIssueFormDTO, Coupon.class);
        if (isBegin) {
            // 设置优惠券为发放中，领取优惠券时根据该状态就可以查询到该优惠券
            copyBean.setStatus(CouponStatus.ISSUING);
            copyBean.setIssueBeginTime(LocalDateTime.now());
        } else {
            // 已指定优惠券开始定时发放，还未开始
            copyBean.setStatus(CouponStatus.UN_ISSUE);
        }

        couponMapper.updateById(copyBean);
    }

    @Override
    @Transactional
    public void updateCoupon(CouponFormDTO couponFormDTO) {
        Coupon coupon = couponMapper.selectById(couponFormDTO.getId());
        if (coupon == null) {
            throw new BadRequestException("优惠券不存在!");
        }
        if (coupon.getStatus() != CouponStatus.DRAFT) {
            throw new BadRequestException("优惠券状态异常!");
        }
        couponMapper.updateById(BeanUtils.copyBean(couponFormDTO, Coupon.class));
    }

    @Override
    public void deleteCoupon(Long id) {
        Coupon coupon = couponMapper.selectById(id);
        if (coupon == null) {
            throw new BadRequestException("优惠券不存在!");
        }
        if (coupon.getStatus() != CouponStatus.DRAFT) {
            throw new BadRequestException("优惠券状态异常!");
        }
        couponMapper.deleteById(id);
    }

    @Override
    public CouponDetailVO queryCouponDetailById(Long id) {
        Coupon coupon = couponMapper.selectById(id);
        if (coupon == null) {
            throw new BadRequestException("优惠券不存在!");
        }
        return BeanUtils.copyBean(coupon, CouponDetailVO.class);
    }

    @Override
    @Transactional
    public void issueCouponsBatch(List<Coupon> records) {
        // 更新优惠券
        records.forEach(coupon -> {
            coupon.setStatus(CouponStatus.ISSUING);
            coupon.setIssueBeginTime(LocalDateTime.now());
        });
        // 批量更新
        MybatisBatch<Coupon> mybatisBatch = new MybatisBatch<>(sqlSessionFactory, records);
        MybatisBatch.Method<Coupon> method = new MybatisBatch.Method<>(CouponMapper.class);
        mybatisBatch.execute(method.updateById());
    }

    @Override
    public void stopIssueCoupons(List<Coupon> records) {
        // 更新优惠券
        records.forEach(coupon -> {
            coupon.setStatus(CouponStatus.FINISHED);
            coupon.setIssueEndTime(LocalDateTime.now());
        });
        // 批量更新
        MybatisBatch<Coupon> mybatisBatch = new MybatisBatch<>(sqlSessionFactory, records);
        MybatisBatch.Method<Coupon> method = new MybatisBatch.Method<>(CouponMapper.class);
        mybatisBatch.execute(method.updateById());
    }

    @Override
    @Transactional
    public void pauseCouponIssue(Long id) {
        Coupon coupon = couponMapper.selectById(id);
        if (coupon == null) {
            throw new BadRequestException("优惠券不存在!");
        }
        if (coupon.getStatus() != CouponStatus.ISSUING) {
            throw new BadRequestException("优惠券状态异常!");
        }
        coupon.setStatus(CouponStatus.PAUSE);
        couponMapper.updateById(coupon);
    }

    @Override
    public List<CouponVO> queryIssuingCoupons() {
        // 查询发放中的优惠券且领取方式为手动
        LambdaQueryWrapper<Coupon> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Coupon::getStatus, CouponStatus.ISSUING)
                .eq(Coupon::getObtainWay, ObtainWay.PUBLIC);
        List<Coupon> coupons = couponMapper.selectList(lambdaQueryWrapper);
        if (CollUtils.isEmpty(coupons)) {
            return CollUtils.emptyList();
        }

        // 获取用户在这批券里已领取了哪些优惠券
        Long userId = UserContext.getUserId();
        LambdaQueryWrapper<UserCoupon> userCouponLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userCouponLambdaQueryWrapper.eq(UserCoupon::getUserId, userId)
                .in(UserCoupon::getCouponId, coupons.stream().map(Coupon::getId).toList());
        List<UserCoupon> userCoupons = userCouponMapper.selectList(userCouponLambdaQueryWrapper);
        // 用户领取的优惠券数量
        Map<Long, Long> userCouponMap = userCoupons.stream()
                .collect(Collectors.groupingBy(UserCoupon::getCouponId, Collectors.counting()));
        // 用户已领取未使用的优惠券数量
        Map<Long, Long> userUnUsedCouponMap = userCoupons.stream()
                .filter(userCoupon -> userCoupon.getStatus().equals(UserCouponStatus.UNUSED))
                .collect(Collectors.groupingBy(UserCoupon::getCouponId, Collectors.counting()));

        // 封装VO结果
        List<CouponVO> list = new ArrayList<>(coupons.size());
        for (Coupon coupon : coupons) {
            CouponVO couponVO = BeanUtils.copyBean(coupon, CouponVO.class);
            // 是否可领取：已经领取的优惠券数量 < 总数量，用户已领取数量小于每张用户可领取限制数
            couponVO.setReceived(coupon.getIssueNum() < coupon.getTotalNum() &&
                    userCouponMap.getOrDefault(coupon.getId(), 0L) < coupon.getUserLimit());
            // 是否可以使用：当前用户已经领取并且未使用的优惠券数量 > 0
            couponVO.setAvailable(userUnUsedCouponMap.getOrDefault(coupon.getId(), 0L) > 0);
            list.add(couponVO);
        }

        return list;
    }


}




