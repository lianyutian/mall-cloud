package com.mall.marketing.coupon.domain.vo;

import com.mall.marketing.coupon.enums.DiscountType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 用户端优惠券详情
 *
 * @author lm
 * @since 2024-06-06 16:01:52
 * @version 1.0
 */
@Schema(description = "用户端优惠券详情", name = "CouponVO")
@Getter
@Setter
public class CouponVO {

    @Schema(description = "优惠券id，新增不需要添加，更新必填", name = "id")
    private Long id;
    @Schema(description = "优惠券名称", name = "name")
    private String name;
    @Schema(description = "是否限定使用范围", name = "specific")
    private Boolean specific;

    @Schema(description = "优惠券类型，1：每满减，2：折扣，3：无门槛，4：普通满减", name = "discountType")
    private DiscountType discountType;
    @Schema(description = "折扣门槛，0代表无门槛", name = "thresholdAmount")
    private Integer thresholdAmount;
    @Schema(description = "折扣值，满减填抵扣金额；打折填折扣值：80标示打8折", name = "discountValue")
    private Integer discountValue;
    @Schema(description = "最大优惠金额", name = "maxDiscountAmount")
    private Integer maxDiscountAmount;

    @Schema(description = "有效天数", name = "termDays")
    private Integer termDays;
    @Schema(description = "使用有效期结束时间", name = "termEndTime")
    private LocalDateTime termEndTime;

    @Schema(description = "是否可以使用", name = "available")
    private Boolean available;

    @Schema(description = "是否可以领取", name = "received")
    private Boolean received;
}
