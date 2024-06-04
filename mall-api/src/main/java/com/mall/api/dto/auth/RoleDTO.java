package com.mall.api.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author liming
 * @version 1.0
 * @since 2024/4/26 17:08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Schema(description = "角色实体", name = "RoleDTO")
public class RoleDTO {
    /**
     * 主键
     */
    @Schema(description = "主键", example = "1", name = "id")
    private Long id;

    /**
     * 角色代号，例如：admin
     */
    @Schema(description = "角色代号", example = "admin", name = "code")
    private String code;

    /**
     * 角色描述
     */
    @Schema(description = "角色名称", example = "教师", name = "name")
    private String name;
}
