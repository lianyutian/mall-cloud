package com.mall.product.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 购物车中购物项
 *
 * @author lm
 * @since 2024-06-14 17:17:25
 * @version 1.0
 */
@Data
@Schema(description = "购物项", name = "CartItemVO")
public class CartItemVO {

    /**
     * 商品id
     */
    private Long productId;

    /**
     * 购买数量
     */
    private Integer buyNum;

    /**
     * 商品标题
     */
    private String productTitle;

    /**
     * 图片
     */
    private String productImg;

    /**
     * 商品单价
     */
    private BigDecimal amount;

    /**
     * 总价格，单价+数量
     */
    private BigDecimal totalAmount;


    /**
     * 商品单价 * 购买数量
     *
     * @return 总价
     */
    public BigDecimal getTotalAmount() {
        return this.amount.multiply(new BigDecimal(this.buyNum));
    }
}
