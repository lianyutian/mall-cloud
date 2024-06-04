package com.mall.auth.controller;

import com.mall.api.dto.user.LoginFormDTO;
import com.mall.auth.common.constants.JwtConstants;
import com.mall.auth.service.IAccountService;
import com.mall.common.exceptions.BadRequestException;
import com.mall.common.utils.WebUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liming
 * @version 1.0
 * @since 2024/3/28 10:52
 */
@RestController
@RequestMapping("/accounts")
@Tag(name = "用户登录相关接口")
@RequiredArgsConstructor
public class AccountController {

    private final IAccountService accountService;

    @Operation(summary = "登录并获取token")
    @PostMapping(value = "/login")
    public String loginByPw(@RequestBody LoginFormDTO loginFormDTO) {
        return accountService.loginByPw(loginFormDTO, false);
    }

    @Operation(summary = "退出登录")
    @PostMapping(value = "/logout")
    public void logout() {
        accountService.logout(JwtConstants.REFRESH_HEADER);
    }

    @Operation(summary = "刷新token")
    @GetMapping(value = "/refresh")
    public String refreshToken(
            @CookieValue(value = JwtConstants.REFRESH_HEADER, required = false) String refreshToken
    ) {
        if (refreshToken == null) {
            throw new BadRequestException("登录超时");
        }
        String host = WebUtils.getHeader("origin");
        if (host == null) {
            throw new BadRequestException("登录超时");
        }
        return accountService.refreshToken(WebUtils.cookieBuilder().decode(refreshToken));
    }

}
