package com.mall.product.controller;

import com.mall.product.domain.vo.CartVO;
import com.mall.product.service.CartService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 购物车控制器
 *
 * @author lm
 * @since 2024-06-14 17:00:36
 * @version 1.0
 */
@RestController
@RequestMapping("cart")
@AllArgsConstructor
public class CartController {

    private final CartService cartService;

    public void addProductToCart(@RequestBody List<CartVO> ) {

    }
}
