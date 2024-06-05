package com.mall.marketing.coupon.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mall.common.domain.dto.PageDTO;
import com.mall.marketing.coupon.domain.dto.CouponFormDTO;
import com.mall.marketing.coupon.domain.query.CouponQuery;
import com.mall.marketing.coupon.domain.vo.CouponPageVO;
import com.mall.marketing.coupon.service.CouponService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public PageDTO<CouponPageVO> queryCouponsByPage(CouponQuery query) {
        return couponService.queryCouponsByPage(query);
    }
}
