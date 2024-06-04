package com.mall.user.controller;

import com.mall.user.domain.dto.UserDTO;
import com.mall.user.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
public class UserController {

    private final UserService userService;

    @PostMapping("saveUser")
    public Long saveUser(@Valid @RequestBody UserDTO userDTO) {
        return userService.saveUser(userDTO);
    }
}
