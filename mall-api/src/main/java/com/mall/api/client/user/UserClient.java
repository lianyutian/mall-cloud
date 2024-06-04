package com.mall.api.client.user;

import com.mall.api.client.user.fallback.UserClientFallback;
import com.mall.api.dto.user.LoginFormDTO;
import com.mall.api.dto.user.UserDTO;
import com.mall.common.domain.dto.LoginUserDTO;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author liming
 * @version 1.0
 * @since 2024/3/28 14:18
 */
@FeignClient(value = "user-service", fallbackFactory = UserClientFallback.class)
public interface UserClient {
    /**
     * 查询登录用户信息
     *
     * @param loginDTO 登录信息
     * @return 用户详情
     */
    @PostMapping("/user/queryLoginUser")
    LoginUserDTO queryLoginUser(@RequestBody LoginFormDTO loginDTO);

    /**
     * 根据id批量查询用户信息
     *
     * @param ids 用户id集合
     * @return 用户集合
     */
    @Hidden
    @GetMapping("/user/list")
    List<UserDTO> queryUserDetailByIds(@RequestParam("ids") Iterable<Long> ids);
}
