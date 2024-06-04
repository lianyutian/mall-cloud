package com.mall.user.controller;

import com.mall.api.dto.user.LoginFormDTO;
import com.mall.common.domain.dto.LoginUserDTO;
import com.mall.user.domain.dto.UserDTO;
import com.mall.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * UserController
 *
 * @author lm
 * @since 2024-06-04 17:06:38
 * @version 1.0
 */
@RequestMapping("user")
@RestController
@AllArgsConstructor
@Tag(name = "用户接口")
public class UserController {

    private final UserService userService;

    @PostMapping("saveUser")
    @Operation(summary = "保存用户")
    public Long saveUser(@Valid @RequestBody UserDTO userDTO) {
        userDTO.setId(null);
        return userService.saveUser(userDTO);
    }

    @PostMapping("queryLoginUser")
    @Operation(summary = "查询用户登录信息")
    LoginUserDTO queryLoginUser(@Valid @RequestBody LoginFormDTO loginDTO) {
        return userService.queryLoginUser(loginDTO);
    }

    @PostMapping("queryUser")
    @Operation(summary = "查询用户信息")
    public UserDTO queryUserById(Long userId) {
        return userService.queryUserById(userId);
    }
}
