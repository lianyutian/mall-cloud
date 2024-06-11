package com.mall.marketing.coupon.handler;

import com.mall.common.autoconfigure.mq.EnhanceMessageHandler;
import com.mall.marketing.coupon.domain.message.UserCouponMessage;
import com.mall.marketing.coupon.service.UserCouponService;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * 优惠券消息处理
 *
 * @author lm
 * @since 2024-06-11 16:49:57
 * @version 1.0
 */
@Component
@RocketMQMessageListener(
        consumerGroup = "signIn_point_group",
        topic = "learning_topic",
        selectorExpression = "sign_in",
        consumeThreadMax = 5 //默认是64个线程并发消息，配置 consumeThreadMax 参数指定并发消费线程数，避免太大导致资源不够
)
@RequiredArgsConstructor
public class CouponMessageHandler extends EnhanceMessageHandler<UserCouponMessage> implements RocketMQListener<UserCouponMessage> {

    private final UserCouponService userCouponService;

    @Override
    protected void handleMessage(UserCouponMessage message) throws Exception {
        userCouponService.checkAndCreateUserCoupon(message);
    }

    @Override
    protected void handleMaxRetriesExceeded(UserCouponMessage message) {

    }

    @Override
    protected boolean isRetry() {
        return false;
    }

    @Override
    protected boolean throwException() {
        return false;
    }

    @Override
    public void onMessage(UserCouponMessage userCouponMessage) {
        super.dispatchMessage(userCouponMessage);
    }
}
