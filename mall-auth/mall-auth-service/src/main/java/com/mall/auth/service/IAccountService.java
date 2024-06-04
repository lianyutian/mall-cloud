package com.mall.auth.service;

import com.mall.api.dto.user.LoginFormDTO;

/**
 * @author liming
 * @version 1.0
 * @since 2024/3/28 14:14
 */
public interface IAccountService {

    /**
     * 根据账号名密码登录
     *
     * @param loginFormDTO 登录表单
     * @param isAdmin 是否管理界面
     * @return 登录结果
     */
    String loginByPw(LoginFormDTO loginFormDTO, boolean isAdmin);

    /**
     * 退出登录
     *
     * @param name headerName
     */
    void logout(String name);

    /**
     * 刷新token
     *
     * @param token token
     * @return 刷新后token
     */
    String refreshToken(String token);
}
