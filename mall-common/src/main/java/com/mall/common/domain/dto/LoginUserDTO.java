package com.mall.common.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * @author liming
 * @version 1.0
 * @since 2024/3/28 14:23
 */
@Getter
@Setter
@Schema(description = "登录用户信息", name = "LoginUserDTO")
public class LoginUserDTO {
    /**
     * 用户id
     */
    @Schema(description = "用户id", name = "userId", example = "1")
    private Long userId;

    /**
     * 是否记住
     */
    @Schema(description = "是否记住", name = "rememberMe", example = "true")
    private Boolean rememberMe;
}
