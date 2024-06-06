package com.mall.marketing.coupon.controller;

import com.mall.marketing.coupon.service.UserCouponService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户优惠券接口
 *
 * @author lm
 * @since 2024-06-05 16:43:36
 * @version 1.0
 */
@RestController
@RequestMapping("usercoupon")
@AllArgsConstructor
public class UserCouponController {

    private final UserCouponService userCouponService;
}
