package com.mall.product.controller;

import com.mall.product.domain.vo.BannerVO;
import com.mall.product.service.BannerService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 轮播图
 *
 * @author lm
 * @since 2024-06-13 16:22:56
 * @version 1.0
 */
@RestController
@RequestMapping("banner")
@AllArgsConstructor
public class BannerController {

    private final BannerService bannerService;

    @Operation(summary = "查询轮播图")
    @PostMapping("queryBanner")
    public List<BannerVO> queryBanner() {
        return bannerService.queryBanner();
    }
}
