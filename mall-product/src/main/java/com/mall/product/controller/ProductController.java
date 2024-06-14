package com.mall.product.controller;

import com.mall.common.domain.dto.PageDTO;
import com.mall.product.domain.dto.ProductVO;
import com.mall.product.domain.query.ProductPageQuery;
import com.mall.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 *
 * @author lm
 * @since 2024-06-13 16:43:28
 * @version 1.0
 */
@RestController
@RequestMapping("product")
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "查询商品详情")
    @PostMapping("queryProductByPage")
    public PageDTO<ProductVO> queryProductByPage(@RequestBody ProductPageQuery productPageQuery) {
        return productService.queryProductByPage(productPageQuery);
    }
}
