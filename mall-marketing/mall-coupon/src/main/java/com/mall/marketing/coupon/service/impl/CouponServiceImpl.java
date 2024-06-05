package com.mall.marketing.coupon.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mall.common.domain.dto.PageDTO;
import com.mall.common.utils.BeanUtils;
import com.mall.common.utils.CollUtils;
import com.mall.common.utils.StringUtils;
import com.mall.marketing.coupon.domain.dto.CouponFormDTO;
import com.mall.marketing.coupon.domain.po.Coupon;
import com.mall.marketing.coupon.domain.query.CouponQuery;
import com.mall.marketing.coupon.domain.vo.CouponPageVO;
import com.mall.marketing.coupon.mapper.CouponMapper;
import com.mall.marketing.coupon.service.CouponService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
* @author lm
* @description 针对表【coupon(优惠券的规则信息)】的数据库操作Service实现
* @createDate 2024-06-05 15:15:05
*/
@Service
@AllArgsConstructor
public class CouponServiceImpl implements CouponService {

    private final CouponMapper couponMapper;

    @Override
    @Transactional
    public void saveCoupon(CouponFormDTO couponFormDTO) {
        Coupon coupon = BeanUtils.copyBean(couponFormDTO, Coupon.class);
        couponMapper.insert(coupon);
    }

    @Override
    public PageDTO<CouponPageVO> queryCouponsByPage(CouponQuery query) {
        Integer status = query.getStatus();
        String name = query.getName();
        Integer type = query.getType();
        // 1.分页查询
        LambdaQueryWrapper<Coupon> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper
                .eq(type != null, Coupon::getDiscountType, type)
                .eq(status != null, Coupon::getStatus, status)
                .like(StringUtils.isNotBlank(name), Coupon::getName, name);

        Page<Coupon> couponPage = couponMapper.selectPage(query.toMpPageDefaultSortByCreateTimeDesc(), lambdaQueryWrapper);

        List<Coupon> couponList = couponPage.getRecords();
        if (CollUtils.isEmpty(couponList)) {
            return PageDTO.empty(couponPage);
        }

        List<CouponPageVO> couponPageVOList = BeanUtils.copyList(couponList, CouponPageVO.class);
        return PageDTO.of(couponPage, couponPageVOList);
    }
}




