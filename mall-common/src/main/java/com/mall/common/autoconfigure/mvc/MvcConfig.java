package com.mall.common.autoconfigure.mvc;

import com.mall.common.autoconfigure.filters.RequestIdFilter;
import com.mall.common.autoconfigure.mvc.advice.CommonExceptionAdvice;
import com.mall.common.autoconfigure.mvc.advice.WrapperResponseBodyAdvice;
import com.mall.common.autoconfigure.mvc.converter.WrapperResponseMessageConverter;
import jakarta.servlet.Filter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author : [lm]
 * @version : [v1.0]
 * @createTime : [2024/4/1 22:08]
 */
@ConditionalOnClass({CommonExceptionAdvice.class, Filter.class})
@Configuration
public class MvcConfig implements WebMvcConfigurer {
    /**
     * 通用的ControllerAdvice异常处理器
     */
    @Bean
    public CommonExceptionAdvice commonExceptionAdvice() {
        return new CommonExceptionAdvice();
    }

    @Bean
    public RequestIdFilter requestIdFilter() {
        return new RequestIdFilter();
    }

    @Bean
    @ConditionalOnMissingClass("org.springframework.cloud.gateway.filter.GlobalFilter")
    public WrapperResponseMessageConverter wrapperResponseMessageConverter(
            MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter) {
        return new WrapperResponseMessageConverter(mappingJackson2HttpMessageConverter);
    }

    @Bean
    public WrapperResponseBodyAdvice wrapperResponseBodyAdvice() {
        return new WrapperResponseBodyAdvice();
    }
}
