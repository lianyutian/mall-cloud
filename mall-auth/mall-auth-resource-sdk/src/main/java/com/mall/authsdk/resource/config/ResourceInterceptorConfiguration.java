package com.mall.authsdk.resource.config;


import com.mall.authsdk.resource.interceptors.LoginAuthInterceptor;
import com.mall.authsdk.resource.interceptors.UserInfoInterceptor;
import com.mall.common.utils.CollUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author : [lm]
 * @version : [v1.0]
 * @createTime : [2024/3/28 20:37]
 */
@Configuration
@EnableConfigurationProperties(ResourceAuthProperties.class)
public class ResourceInterceptorConfiguration implements WebMvcConfigurer {
    private final ResourceAuthProperties authProperties;

    @Autowired
    public ResourceInterceptorConfiguration(ResourceAuthProperties resourceAuthProperties) {
        this.authProperties = resourceAuthProperties;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 1.添加用户信息拦截器
        registry.addInterceptor(new UserInfoInterceptor()).order(0);
        // 2.是否需要做登录拦截
        if (!authProperties.getEnable()) {
            // 无需登录拦截
            return;
        }
        // 2.添加登录拦截器
        InterceptorRegistration registration = registry.addInterceptor(new LoginAuthInterceptor()).order(1);
        // 2.1.添加拦截器路径
        if (CollUtils.isNotEmpty(authProperties.getIncludeLoginPaths())) {
            registration.addPathPatterns(authProperties.getIncludeLoginPaths());
        }
        // 2.2.添加排除路径
        if (CollUtils.isNotEmpty(authProperties.getExcludeLoginPaths())) {
            registration.excludePathPatterns(authProperties.getExcludeLoginPaths());
        }
        // 2.3.排除swagger路径
        registration.excludePathPatterns(
                "/error/**",
                "/v2/**",
                "/v3/**",
                "/swagger-resources/**",
                "/webjars/**",
                "/doc.html"
        );
    }
}
