package com.mall.marketing.coupon.controller;

import com.mall.common.domain.dto.PageDTO;
import com.mall.marketing.coupon.domain.dto.CouponFormDTO;
import com.mall.marketing.coupon.domain.dto.CouponIssueFormDTO;
import com.mall.marketing.coupon.domain.query.CouponQuery;
import com.mall.marketing.coupon.domain.vo.CouponDetailVO;
import com.mall.marketing.coupon.domain.vo.CouponPageVO;
import com.mall.marketing.coupon.service.CouponService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * CouponController
 *
 * @author lm
 * @since 2024-06-05 15:06:57
 * @version 1.0
 */
@RestController
@RequestMapping("coupon")
@AllArgsConstructor
@Tag(name = "优惠券相关接口")
public class CouponController {

    private final CouponService couponService;

    @Operation(summary = "添加优惠券")
    @PostMapping("saveCoupon")
    public void saveCoupon(@Valid @RequestBody CouponFormDTO couponFormDTO) {
        couponService.saveCoupon(couponFormDTO);
    }

    @Operation(summary = "优惠券分页查询")
    @PostMapping("queryCouponsByPage")
    public PageDTO<CouponPageVO> queryCouponsByPage(@RequestBody CouponQuery query) {
        return couponService.queryCouponsByPage(query);
    }

    @Operation(summary = "发放优惠券")
    @PostMapping("issueCoupon")
    public void issueCoupon(@Valid @RequestBody CouponIssueFormDTO couponIssueFormDTO) {
        couponService.issueCoupon(couponIssueFormDTO);
    }

    @Operation(summary = "更新优惠券")
    @PostMapping("updateCoupon")
    public void updateCoupon(@Valid @RequestBody CouponFormDTO couponFormDTO) {
        couponService.updateCoupon(couponFormDTO);
    }

    @Operation(summary = "删除优惠券")
    @PostMapping("deleteCoupon")
    public void deleteCoupon(Long id) {
        couponService.deleteCoupon(id);
    }

    @Operation(summary = "优惠券详情")
    @GetMapping("queryCouponDetailById/{id}")
    public CouponDetailVO queryCouponDetailById(@PathVariable Long id) {
        return couponService.queryCouponDetailById(id);
    }
}
