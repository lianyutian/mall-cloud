package com.mall.product.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * BannerVO
 *
 * @author lm
 * @since 2024-06-13 16:32:30
 * @version 1.0
 */
@Schema(description = "轮播图", name = "BannerVO")
@Data
public class BannerVO {
    @Schema(description = "轮播图id", name = "id")
    private Integer id;

    @Schema(description = "轮播图图片", name = "img")
    private String img;

    @Schema(description = "轮播图跳转链接", name = "url")
    private String url;

    @Schema(description = "轮播图权重", name = "weight")
    private Integer weight;
}
