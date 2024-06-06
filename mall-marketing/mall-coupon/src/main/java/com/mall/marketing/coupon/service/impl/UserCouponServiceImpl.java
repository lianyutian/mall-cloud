package com.mall.marketing.coupon.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mall.common.utils.BeanUtils;
import com.mall.common.utils.CollUtils;
import com.mall.common.utils.UserContext;
import com.mall.marketing.coupon.domain.po.Coupon;
import com.mall.marketing.coupon.domain.po.UserCoupon;
import com.mall.marketing.coupon.domain.vo.CouponVO;
import com.mall.marketing.coupon.enums.CouponStatus;
import com.mall.marketing.coupon.enums.ObtainWay;
import com.mall.marketing.coupon.enums.UserCouponStatus;
import com.mall.marketing.coupon.mapper.CouponMapper;
import com.mall.marketing.coupon.mapper.UserCouponMapper;
import com.mall.marketing.coupon.service.UserCouponService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author lm
 * @description 针对表【user_coupon(用户领取优惠券的记录，是真正使用的优惠券信息)】的数据库操作Service实现
 * @createDate 2024-06-06 15:54:53
 */
@Service
@AllArgsConstructor
public class UserCouponServiceImpl implements UserCouponService {

    private final CouponMapper couponMapper;
    private final UserCouponMapper userCouponMapper;


}




