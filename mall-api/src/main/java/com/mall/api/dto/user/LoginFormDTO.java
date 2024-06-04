package com.mall.api.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * @author liming
 * @version 1.0
 * @since 2024/3/28 13:59
 */
@Data
@Schema(description = "登录表单实体", name = "LoginFormDTO")
public class LoginFormDTO implements Serializable {
    @Schema(description = "登录方式：1-密码登录; 2-验证码登录", name = "type", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    private Integer type;

    @Schema(description = "邮箱", name = "mail", example = "1436159527@qq.com")
    @Email
    private String mail;

    @Schema(description = "密码", name = "password", example = "admin", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    private String password;

    @Schema(description = "7天免密登录", name = "rememberMe", example = "true")
    private Boolean rememberMe;
}
