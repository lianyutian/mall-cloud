package com.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mall.common.utils.BeanUtils;
import com.mall.common.utils.CollUtils;
import com.mall.product.domain.po.Banner;
import com.mall.product.domain.vo.BannerVO;
import com.mall.product.mapper.BannerMapper;
import com.mall.product.service.BannerService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lm
 * @description 针对表【banner】的数据库操作Service实现
 * @createDate 2024-06-13 16:21:29
 */
@Service
@AllArgsConstructor
public class BannerServiceImpl implements BannerService {

    private final BannerMapper bannerMapper;

    @Override
    public List<BannerVO> queryBanner() {
        QueryWrapper<Banner> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("weight");
        List<Banner> banners = bannerMapper.selectList(queryWrapper);

        if (CollUtils.isEmpty(banners)) {
            return CollUtils.emptyList();
        }

        return BeanUtils.copyList(banners, BannerVO.class);
    }
}




