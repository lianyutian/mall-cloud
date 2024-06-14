package com.mall.product.domain.query;

import com.mall.common.domain.query.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 商品分页查询
 *
 * @author lm
 * @since 2024-06-13 16:46:44
 * @version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Schema(description = "商品分页查询", name = "ProductPageQuery")
@Data
public class ProductPageQuery extends PageQuery {
    @Schema(description = "商品类别", name = "id")
    private Long id;
}
