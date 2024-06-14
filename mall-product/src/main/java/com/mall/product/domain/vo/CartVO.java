package com.mall.product.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.List;

/**
 * 购物车
 *
 * @author lm
 * @since 2024-06-14 17:16:23
 * @version 1.0
 */
@Schema(description = "购物车", name = "CartVO")
public class CartVO {
    /**
     * 购物项
     */

    private List<CartItemVO> cartItems;


    /**
     * 购买总件数
     */
    private Integer totalNum;

    /**
     * 购物车总价格
     */
    private BigDecimal totalAmount;

    /**
     * 购物车实际支付价格
     */
    private BigDecimal realPayAmount;

    /**
     * 总件数
     * @return 总件数
     */
    public Integer getTotalNum() {
        if (this.cartItems != null) {
            int total = cartItems.stream().mapToInt(CartItemVO::getBuyNum).sum();
            return total;
        }
        return 0;
    }

    /**
     * 总价格
     * @return 总价格
     */
    public BigDecimal getTotalAmount() {
        BigDecimal amount = new BigDecimal("0");
        if (this.cartItems != null) {
            for (CartItemVO cartItemVO : cartItems) {
                BigDecimal itemTotalAmount = cartItemVO.getTotalAmount();
                amount = amount.add(itemTotalAmount);
            }
        }
        return amount;
    }

    /**
     * 购物车里面实际支付的价格
     * @return 实际支付的价格
     */
    public BigDecimal getRealPayAmount() {
        BigDecimal amount = new BigDecimal("0");
        if (this.cartItems != null) {
            for (CartItemVO cartItemVO : cartItems) {
                BigDecimal itemTotalAmount = cartItemVO.getTotalAmount();
                amount = amount.add(itemTotalAmount);
            }
        }
        return amount;
    }

    public List<CartItemVO> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItemVO> cartItems) {
        this.cartItems = cartItems;
    }
}
