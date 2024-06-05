package com.mall.marketing.coupon.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mall.common.domain.dto.PageDTO;
import com.mall.common.exceptions.BadRequestException;
import com.mall.common.exceptions.MallException;
import com.mall.common.utils.BeanUtils;
import com.mall.common.utils.CollUtils;
import com.mall.common.utils.StringUtils;
import com.mall.marketing.coupon.domain.dto.CouponFormDTO;
import com.mall.marketing.coupon.domain.dto.CouponIssueFormDTO;
import com.mall.marketing.coupon.domain.po.Coupon;
import com.mall.marketing.coupon.domain.query.CouponQuery;
import com.mall.marketing.coupon.domain.vo.CouponDetailVO;
import com.mall.marketing.coupon.domain.vo.CouponPageVO;
import com.mall.marketing.coupon.enums.CouponStatus;
import com.mall.marketing.coupon.mapper.CouponMapper;
import com.mall.marketing.coupon.service.CouponService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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

    @Override
    @Transactional
    public void issueCoupon(CouponIssueFormDTO couponIssueFormDTO) {
        Coupon coupon = couponMapper.selectById(couponIssueFormDTO.getId());
        if (coupon == null) {
            throw new BadRequestException("优惠券不存在!");
        }

        if (coupon.getStatus() != CouponStatus.DRAFT && coupon.getStatus() != CouponStatus.PAUSE) {
            throw new BadRequestException("优惠券状态异常!");
        }

        // 是否立即发放
        LocalDateTime issueBeginTime = couponIssueFormDTO.getIssueBeginTime();
        boolean isBegin = issueBeginTime == null || !issueBeginTime.isBefore(LocalDateTime.now());
        Coupon copyBean = BeanUtils.copyBean(couponIssueFormDTO, Coupon.class);
        if (isBegin) {
            // 设置优惠券为发放中，领取优惠券时根据该状态就可以查询到该优惠券
            copyBean.setStatus(CouponStatus.ISSUING);
            copyBean.setIssueBeginTime(LocalDateTime.now());
        } else {
            // 已指定优惠券开始定时发放，还未开始
            copyBean.setStatus(CouponStatus.UN_ISSUE);
        }

        couponMapper.updateById(copyBean);
    }

    @Override
    @Transactional
    public void updateCoupon(CouponFormDTO couponFormDTO) {
        Coupon coupon = couponMapper.selectById(couponFormDTO.getId());
        if (coupon == null) {
            throw new BadRequestException("优惠券不存在!");
        }
        if (coupon.getStatus() != CouponStatus.DRAFT) {
            throw new BadRequestException("优惠券状态异常!");
        }
        couponMapper.updateById(BeanUtils.copyBean(couponFormDTO, Coupon.class));
    }

    @Override
    public void deleteCoupon(Long id) {
        Coupon coupon = couponMapper.selectById(id);
        if (coupon == null) {
            throw new BadRequestException("优惠券不存在!");
        }
        if (coupon.getStatus() != CouponStatus.DRAFT) {
            throw new BadRequestException("优惠券状态异常!");
        }
        couponMapper.deleteById(id);
    }

    @Override
    public CouponDetailVO queryCouponDetailById(Long id) {
        Coupon coupon = couponMapper.selectById(id);
        if (coupon == null) {
            throw new BadRequestException("优惠券不存在!");
        }
        return BeanUtils.copyBean(coupon, CouponDetailVO.class);
    }
}




