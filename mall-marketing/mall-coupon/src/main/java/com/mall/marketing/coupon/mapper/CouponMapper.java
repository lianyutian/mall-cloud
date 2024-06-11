package com.mall.marketing.coupon.mapper;

import com.mall.marketing.coupon.domain.po.Coupon;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Update;

/**
* @author lm
* @description 针对表【coupon(优惠券的规则信息)】的数据库操作Mapper
* @createDate 2024-06-05 15:15:05
* @Entity com.mall.marketing.coupon.domain.po.Coupon
*/
public interface CouponMapper extends BaseMapper<Coupon> {

    /**
     * 优惠券已发数量+1
     *
     * @param id 优惠券id
     */
    @Update("UPDATE coupon SET issue_num = issue_num + 1 WHERE id = #{id} and issue_num < total_num")
    int incrIssueNum(Long id);
}




