package com.mall.marketing.coupon.service;

import com.mall.common.domain.dto.PageDTO;
import com.mall.marketing.coupon.domain.message.UserCouponMessage;
import com.mall.marketing.coupon.domain.po.Coupon;
import com.mall.marketing.coupon.domain.query.UserCouponQuery;
import com.mall.marketing.coupon.domain.vo.CouponVO;

/**
* @author lm
* @description 针对表【user_coupon(用户领取优惠券的记录，是真正使用的优惠券信息)】的数据库操作Service
* @createDate 2024-06-06 15:54:53
*/
public interface UserCouponService {

    /**
     * 领取优惠券
     *
     * @param id 优惠券id
     */
    void receiveCoupon(Long id, Long userId);

    /**
     * 查询用户优惠券
     *
     * @return 用户优惠券
     */
    PageDTO<CouponVO> queryUserCoupons(UserCouponQuery userCouponQuery);

    /**
     * 创建用户优惠券
     *
     * @param userCouponMessage      用户优惠券消息
     */
    void checkAndCreateUserCoupon(UserCouponMessage userCouponMessage);
}
