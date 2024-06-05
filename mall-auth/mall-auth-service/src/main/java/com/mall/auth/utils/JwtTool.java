package com.mall.auth.utils;

import cn.hutool.core.exceptions.ValidateException;
import cn.hutool.core.lang.UUID;
import cn.hutool.json.JSONObject;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTValidator;
import cn.hutool.jwt.signers.JWTSigner;
import cn.hutool.jwt.signers.JWTSignerUtil;
import com.mall.auth.common.constants.AuthErrorInfo;
import com.mall.auth.common.constants.JwtConstants;
import com.mall.common.domain.dto.LoginUserDTO;
import com.mall.common.exceptions.BadRequestException;
import com.mall.common.utils.AssertUtils;
import com.mall.common.utils.BooleanUtils;
import com.mall.common.utils.StringUtils;
import com.mall.common.utils.UserContext;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.time.Duration;
import java.util.Date;

import static com.mall.auth.common.constants.JwtConstants.JWT_REFRESH_TTL;
import static com.mall.auth.common.constants.JwtConstants.JWT_TOKEN_TTL;


/**
 * @author : [lm]
 * @version : [v1.0]
 * @createTime : [2024/3/28 20:15]
 */
@Component
public class JwtTool {
    private final StringRedisTemplate stringRedisTemplate;
    private final JWTSigner jwtSigner;

    public JwtTool(StringRedisTemplate stringRedisTemplate, KeyPair keyPair) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.jwtSigner = JWTSignerUtil.createSigner("rs256", keyPair);
    }

    /**
     * 创建 access-token
     *
     * @param userDTO 用户信息
     * @return access-token
     */
    public String createToken(LoginUserDTO userDTO) {
        // 1.生成jwt
        return JWT.create()
                .setPayload(JwtConstants.PAYLOAD_USER_KEY, userDTO)
                .setExpiresAt(new Date(System.currentTimeMillis() + JWT_TOKEN_TTL.toMillis()))
                .setSigner(jwtSigner)
                .sign();
    }

    /**
     * 创建刷新token，并将token的JTI记录到Redis中
     *
     * @param userDetail 用户信息
     * @return 刷新token
     */
    public String createRefreshToken(LoginUserDTO userDetail) {
        // 1.生成 JTI
        String jti = UUID.randomUUID().toString(true);
        // 2.生成jwt
        // 2.1.如果是记住我，则有效期7天，否则30分钟
        Duration ttl = BooleanUtils.isTrue(userDetail.getRememberMe()) ?
                JwtConstants.JWT_REMEMBER_ME_TTL : JWT_REFRESH_TTL;
        // 2.2.生成refreshToken
        String refreshToken = JWT.create()
                .setJWTId(jti)
                .setPayload(JwtConstants.PAYLOAD_USER_KEY, userDetail)
                .setExpiresAt(new Date(System.currentTimeMillis() + ttl.toMillis()))
                .setSigner(jwtSigner)
                .sign();
        // 3.缓存jti，有效期与token一致，过期或删除JTI后，对应的refresh-token失效
        stringRedisTemplate.opsForValue().set(JwtConstants.JWT_REDIS_KEY_PREFIX + userDetail.getUserId(), jti, ttl);
        return refreshToken;
    }

    /**
     * 解析刷新token
     *
     * @param refreshToken 刷新token
     * @return 解析刷新token得到的用户信息
     */
    public LoginUserDTO parseRefreshToken(String refreshToken) {
        // 1.校验token是否为空
        AssertUtils.isNotNull(refreshToken, AuthErrorInfo.Msg.INVALID_TOKEN);
        // 2.校验并解析jwt
        JWT jwt;
        try {
            jwt = JWT.of(refreshToken).setSigner(jwtSigner);
        } catch (Exception e) {
            throw new BadRequestException(400, AuthErrorInfo.Msg.INVALID_TOKEN, e);
        }
        // 2.校验jwt是否有效
        if (!jwt.verify()) {
            // 验证失败
            throw new BadRequestException(400, AuthErrorInfo.Msg.INVALID_TOKEN);
        }
        // 3.校验是否过期
        try {
            JWTValidator.of(jwt).validateDate();
        } catch (ValidateException e) {
            throw new BadRequestException(400, AuthErrorInfo.Msg.EXPIRED_TOKEN);
        }
        // 4.数据格式校验
        Object userPayload = jwt.getPayload(JwtConstants.PAYLOAD_USER_KEY);
        Object jtiPayload = jwt.getPayload(JwtConstants.PAYLOAD_JTI_KEY);
        if (jtiPayload == null || userPayload == null) {
            // 数据为空
            throw new BadRequestException(400, AuthErrorInfo.Msg.INVALID_TOKEN);
        }

        // 5.数据解析
        LoginUserDTO userDTO;
        try {
            userDTO = ((JSONObject) userPayload).toBean(LoginUserDTO.class);
        } catch (RuntimeException e) {
            // 数据格式有误
            throw new BadRequestException(400, AuthErrorInfo.Msg.INVALID_TOKEN);
        }

        // 6.JTI校验
        String jti = stringRedisTemplate.opsForValue().get(JwtConstants.JWT_REDIS_KEY_PREFIX + userDTO.getUserId());
        if (!StringUtils.equals(jti, jtiPayload.toString())) {
            // jti不一致
            throw new BadRequestException(400, AuthErrorInfo.Msg.INVALID_TOKEN);
        }
        return userDTO;
    }

    /**
     * 清理刷新refresh-token的jti，本质是refresh-token作废
     */
    public void cleanJtiCache() {
        stringRedisTemplate.delete(JwtConstants.JWT_REDIS_KEY_PREFIX + UserContext.getUserId());
    }
}
