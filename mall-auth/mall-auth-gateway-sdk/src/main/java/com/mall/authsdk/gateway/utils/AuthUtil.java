package com.mall.authsdk.gateway.utils;

import cn.hutool.core.exceptions.ValidateException;
import cn.hutool.json.JSONObject;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTValidator;
import com.mall.common.domain.Result;
import com.mall.common.domain.dto.LoginUserDTO;
import com.mall.common.utils.StringUtils;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.AntPathMatcher;

import java.util.HashSet;
import java.util.Set;

import static com.mall.auth.common.constants.AuthErrorInfo.Code.EXPIRED_TOKEN_CODE;
import static com.mall.auth.common.constants.AuthErrorInfo.Code.INVALID_TOKEN_CODE;
import static com.mall.auth.common.constants.AuthErrorInfo.Msg.*;
import static com.mall.auth.common.constants.JwtConstants.PAYLOAD_USER_KEY;

/**
 * auth工具类
 *
 * @author liming
 * @version 1.0
 * @since 2024/3/28 9:09
 */
public class AuthUtil {
    /**
     * 要拦截的路径匹配符的集合
     */
    private Set<String> paths = new HashSet<>();
    
    private final AntPathMatcher antPathMatcher = new AntPathMatcher();
    private final JwtSignerHolder jwtSignerHolder;
    private final StringRedisTemplate stringRedisTemplate;
    //private final BoundHashOperations<String, String, String> hashOps;

    public AuthUtil(JwtSignerHolder jwtSignerHolder, StringRedisTemplate stringRedisTemplate) {
        this.jwtSignerHolder = jwtSignerHolder;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public Result<LoginUserDTO> parseToken(String token) {
        // 1.校验token是否为空
        if(StringUtils.isBlank(token)){
            return Result.error(INVALID_TOKEN_CODE, INVALID_TOKEN);
        }
        JWT jwt;
        try {
            jwt = JWT.of(token).setSigner(jwtSignerHolder.getJwtSigner());
        } catch (Exception e) {
            return Result.error(INVALID_TOKEN_CODE, INVALID_TOKEN);
        }
        // 2.校验jwt是否有效
        if (!jwt.verify()) {
            // 验证失败，返回空
            return Result.error(INVALID_TOKEN_CODE, INVALID_TOKEN);
        }
        // 3.校验是否过期
        try {
            JWTValidator.of(jwt).validateDate();
        } catch (ValidateException e) {
            return Result.error(EXPIRED_TOKEN_CODE, EXPIRED_TOKEN);
        }
        // 4.数据格式校验
        Object userPayload = jwt.getPayload(PAYLOAD_USER_KEY);
        if (userPayload == null) {
            // 数据为空
            return Result.error(INVALID_TOKEN_CODE, INVALID_TOKEN_PAYLOAD);
        }

        // 5.数据解析
        LoginUserDTO userDTO;
        try {
            userDTO = ((JSONObject)userPayload).toBean(LoginUserDTO.class);
        } catch (RuntimeException e) {
            // token格式有误
            return Result.error(INVALID_TOKEN_CODE, INVALID_TOKEN_PAYLOAD);
        }

        // 6.返回
        return Result.ok(userDTO);
    }

    private String findMatchPath(String antPath){
        String matchPath = null;
        for (String pathPattern : paths) {
            if(antPathMatcher.match(pathPattern, antPath)){
                matchPath = pathPattern;
                break;
            }
        }
        return matchPath;
    }
}
