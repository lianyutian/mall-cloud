package com.mall.user.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * UserDTO
 *
 * @author lm
 * @since 2024-06-04 17:09:04
 * @version 1.0
 */
@Setter
@Getter
@Schema(name = "UserDTO", description = "用户信息")
public class UserDTO {
    @Schema(description = "用户id", name = "id", example = "1")
    private Long id;

    @Schema(description = "用户名", name = "name", example = "admin")
    @NotBlank
    private String name;

    @Size(max = 10)
    @Schema(description = "密码", name = "pwd", example = "admin")
    private String pwd;

    @Schema(description = "头像", name = "headImg", example = "default-user-icon.jpg")
    private String headImg;

    @Schema(description = "性别：0-男性，1-女性", name = "sex", example = "0")
    @NotEmpty
    private Integer sex;

    @Schema(description = "积分", name = "points", example = "100")
    private Integer points;

    @Schema(description = "邮箱", name = "mail")
    @Email
    private String mail;
}
