package com.mall.auth.service;

import com.mall.auth.domain.po.LoginRecord;

/**
 * @author : [lm]
 * @version : [v1.0]
 * @createTime : [2024/3/29 20:47]
 */
public interface LoginRecordService {
    /**
     * 保存登录成功记录
     *
     * @param mail 邮箱
     * @param userId 用户id
     */
    void saveLoginSuccessRecord(String mail, Long userId);

    /**
     * 异步保存登录成功记录
     *
     * @param record 登录记录
     */
    void saveAsync(LoginRecord record);
}
