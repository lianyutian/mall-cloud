package com.mall.marketing.coupon.domain.query;

import com.mall.common.domain.query.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * 用户优惠券分页查询参数
 *
 * @author lm
 * @since 2024-06-07 16:34:19
 * @version 1.0
 */
@Getter
@Setter
@Schema(description = "用户优惠券分页查询参数", name = "UserCouponQuery")
public class UserCouponQuery extends PageQuery {
    @NotNull
    @Schema(description = "优惠券状态，1：未使用，2：已使用，3：已过期", name = "status")
    private Integer status;
}
