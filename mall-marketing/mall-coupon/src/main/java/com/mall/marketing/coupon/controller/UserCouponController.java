package com.mall.marketing.coupon.controller;

import com.mall.common.domain.dto.PageDTO;
import com.mall.marketing.coupon.domain.query.UserCouponQuery;
import com.mall.marketing.coupon.domain.vo.CouponVO;
import com.mall.marketing.coupon.service.UserCouponService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    @Operation(summary = "领取优惠券")
    @PostMapping("receiveCoupon/{id}")
    public void receiveCoupon(HttpServletRequest request, @PathVariable Long id) {
        String userId = request.getHeader("user-info");
        userCouponService.receiveCoupon(id, Long.valueOf(userId));
    }

    @Operation(summary = "查询用户优惠券")
    @PostMapping("queryUserCoupons")
    public PageDTO<CouponVO> queryUserCoupons(@Valid @RequestBody UserCouponQuery userCouponQuery) {
        return userCouponService.queryUserCoupons(userCouponQuery);
    }
}
