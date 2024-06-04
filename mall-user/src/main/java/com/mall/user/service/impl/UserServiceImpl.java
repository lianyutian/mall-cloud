package com.mall.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mall.common.exceptions.MallException;
import com.mall.common.utils.BeanUtils;
import com.mall.user.domain.dto.UserDTO;
import com.mall.user.domain.po.User;
import com.mall.user.mapper.UserMapper;
import com.mall.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * @author lm
 * @description 针对表【user(用户表)】的数据库操作Service实现
 * @createDate 2024-06-04 17:02:47
 */
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserMapper userMapper;
    private PasswordEncoder passwordEncoder;

    @Override
    public Long saveUser(UserDTO userDTO) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email", userDTO.getMail());
        User user = userMapper.selectOne(queryWrapper);
        if (user != null) {
            throw new MallException("该邮箱已注册，请直接登录");
        }
        User newUser = BeanUtils.copyBean(userDTO, User.class);
        newUser.setPwd(passwordEncoder.encode(newUser.getPwd()));
        userMapper.insert(newUser);
        return newUser.getId();
    }
}




