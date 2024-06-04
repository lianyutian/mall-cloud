package com.mall.common.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

/**
 * @author liming
 * @version 1.0
 * @since 2024/4/2 14:00
 */
@Schema(description = "DTO基础属性", name = "BaseDTO")
public class BaseDTO {
    @Schema(description = "创建人id", name = "creater")
    public Long creater;
    @Schema(description = "更新人id", name = "updater")
    public Long updater;
    @Schema(description = "创建时间", name = "createTime")
    public LocalDateTime createTime;
    @Schema(description = "更新时间", name = "updateTime")
    public LocalDateTime updateTime;
}
