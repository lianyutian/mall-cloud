package com.mall.marketing.coupon.service;

import com.mall.common.domain.dto.PageDTO;
import com.mall.marketing.coupon.domain.dto.CouponFormDTO;
import com.mall.marketing.coupon.domain.dto.CouponIssueFormDTO;
import com.mall.marketing.coupon.domain.query.CouponQuery;
import com.mall.marketing.coupon.domain.vo.CouponDetailVO;
import com.mall.marketing.coupon.domain.vo.CouponPageVO;

/**
* @author lm
* @description 针对表【coupon(优惠券的规则信息)】的数据库操作Service
* @createDate 2024-06-05 15:15:05
*/
public interface CouponService {

    /**
     * 添加优惠券
     *
     * @param couponFormDTO 优惠券表单实体
     */
    void saveCoupon(CouponFormDTO couponFormDTO);

    /**
     * 优惠券分页查询
     *
     * @param query 优惠券查询参数
     * @return 优惠券分页数据
     */
    PageDTO<CouponPageVO> queryCouponsByPage(CouponQuery query);

    /**
     * 发放优惠券
     *
     * @param couponIssueFormDTO 优惠券发放表单实体
     */
    void issueCoupon(CouponIssueFormDTO couponIssueFormDTO);

    /**
     * 更新优惠券
     *
     * @param couponFormDTO 优惠券表单实体
     */
    void updateCoupon(CouponFormDTO couponFormDTO);

    /**
     * 删除优惠券
     *
     * @param id 优惠券id
     */
    void deleteCoupon(Long id);

    /**
     * 优惠券详情
     *
     * @param id 优惠券id
     * @return 优惠券详情
     */
    CouponDetailVO queryCouponDetailById(Long id);
}
