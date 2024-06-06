package com.mall.marketing.coupon.task;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mall.common.utils.CollUtils;
import com.mall.marketing.coupon.domain.po.Coupon;
import com.mall.marketing.coupon.enums.CouponStatus;
import com.mall.marketing.coupon.mapper.CouponMapper;
import com.mall.marketing.coupon.service.CouponService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 定时发放优惠券任务
 *
 * @author lm
 * @since 2024-06-06 10:09:26
 * @version 1.0
 */
@Slf4j
@Component
@AllArgsConstructor
public class ScheduleIssueCouponsTask {

    private final CouponService couponService;
    private final CouponMapper couponMapper;

    @XxlJob("scheduleIssueCoupons")
    public void scheduleIssueCoupons() {
        log.info("定时发放优惠券任务开始: {}", LocalDateTime.now());
        int pageNo = XxlJobHelper.getShardIndex() + 1;
        int pageSize = Integer.parseInt(XxlJobHelper.getJobParam());

        // 查询<<未开始>>的优惠券
        LambdaQueryWrapper<Coupon> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper
                .eq(Coupon::getStatus, CouponStatus.UN_ISSUE)
                .le(Coupon::getTermBeginTime, LocalDateTime.now());
        Page<Coupon> page = couponMapper.selectPage(new Page<>(pageNo, pageSize), lambdaQueryWrapper);

        // 发放优惠券
        List<Coupon> records = page.getRecords();
        if (CollUtils.isEmpty(records)) {
            return;
        }
        couponService.issueCouponsBatch(records);

        log.info("定时发放优惠券任务结束: {}", LocalDateTime.now());
    }

    @XxlJob("scheduleStopIssueCoupons")
    public void scheduleStopIssueCoupons() {
        log.info("定时结束发放优惠券任务开始: {}", LocalDateTime.now());
        int pageNo = XxlJobHelper.getShardIndex() + 1;
        int pageSize = Integer.parseInt(XxlJobHelper.getJobParam());

        // 查询到期的优惠券
        LambdaQueryWrapper<Coupon> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper
                .eq(Coupon::getStatus, CouponStatus.ISSUING)
                .le(Coupon::getIssueEndTime, LocalDateTime.now());
        Page<Coupon> page = couponMapper.selectPage(new Page<>(pageNo, pageSize), lambdaQueryWrapper);

        // 结束发放优惠券
        List<Coupon> records = page.getRecords();
        if (CollUtils.isEmpty(records)) {
            return;
        }
        couponService.stopIssueCoupons(records);

        log.info("定时结束发放优惠券任务结束: {}", LocalDateTime.now());
    }
}
