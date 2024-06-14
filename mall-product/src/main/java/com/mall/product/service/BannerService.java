package com.mall.product.service;

import com.mall.product.domain.vo.BannerVO;

import java.util.List;

/**
* @author lm
* @description 针对表【banner】的数据库操作Service
* @createDate 2024-06-13 16:21:29
*/
public interface BannerService {

    /**
     * 查询轮播图
     *
     * @return 轮播图
     */
    List<BannerVO> queryBanner();
}
