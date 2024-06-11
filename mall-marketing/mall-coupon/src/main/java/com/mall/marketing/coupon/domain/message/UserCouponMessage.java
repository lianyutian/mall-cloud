package com.mall.marketing.coupon.domain.message;

import com.mall.common.autoconfigure.mq.BaseMessage;
import lombok.Data;

/**
 * UserCouponMessage
 *
 * @author lm
 * @since 2024-06-11 16:41:43
 * @version 1.0
 */
@Data
public class UserCouponMessage extends BaseMessage {
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 优惠券id
     */
    private Long couponId;
}
