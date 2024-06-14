package com.mall.product.service;

import com.mall.common.domain.dto.PageDTO;
import com.mall.product.domain.dto.ProductVO;
import com.mall.product.domain.query.ProductPageQuery;

/**
* @author lm
* @description 针对表【product】的数据库操作Service
* @createDate 2024-06-13 16:43:15
*/
public interface ProductService {

    /**
     * 查询商品详情
     *
     * @param productPageQuery 商品分页查询
     * @return 商品分页数据
     */
    PageDTO<ProductVO> queryProductByPage(ProductPageQuery productPageQuery);
}
