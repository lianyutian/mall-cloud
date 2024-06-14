package com.mall.product.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mall.common.domain.dto.PageDTO;
import com.mall.common.utils.BeanUtils;
import com.mall.common.utils.CollUtils;
import com.mall.product.domain.dto.ProductVO;
import com.mall.product.domain.po.Product;
import com.mall.product.domain.query.ProductPageQuery;
import com.mall.product.mapper.ProductMapper;
import com.mall.product.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lm
 * @description 针对表【product】的数据库操作Service实现
 * @createDate 2024-06-13 16:43:15
 */
@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductMapper productMapper;

    @Override
    public PageDTO<ProductVO> queryProductByPage(ProductPageQuery productPageQuery) {
        Page<Product> productPage = productMapper.
                selectPage(productPageQuery.toMpPageDefaultSortByCreateTimeDesc(), null);

        List<Product> records = productPage.getRecords();
        if (CollUtils.isEmpty(records)) {
            return PageDTO.empty(productPage);
        }

        List<ProductVO> productVOList = BeanUtils.copyList(records, ProductVO.class);
        return PageDTO.of(productPage, productVOList);
    }
}




