package com.mall.user.service;

import com.mall.api.dto.user.LoginFormDTO;
import com.mall.common.domain.dto.LoginUserDTO;
import com.mall.user.domain.dto.UserDTO;
import com.mall.user.domain.po.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author lm
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2024-06-04 17:02:47
*/
public interface UserService {

    /**
     * 保存用户
     *
     * @param userDTO 用户信息
     * @return 用户id
     */
    Long saveUser(UserDTO userDTO);

    /**
     * 查询用户登录信息
     *
     * @param loginDTO 登录信息
     * @return 用户信息
     */
    LoginUserDTO queryLoginUser(LoginFormDTO loginDTO);

    /**
     * 根据id查询用户
     *
     * @param userId 用户id
     * @return 用户信息
     */
    UserDTO queryUserById(Long userId);


}
