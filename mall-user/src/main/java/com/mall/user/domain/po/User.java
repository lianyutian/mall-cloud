package com.mall.user.domain.po;

import com.mall.common.domain.dto.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

/**
 * 用户表
 * @TableName user
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString
public class User extends BaseDTO implements Serializable {
    /**
     * 
     */
    private Long id;

    /**
     * 昵称
     */
    private String name;

    /**
     * 密码
     */
    private String pwd;

    /**
     * 头像
     */
    private String headImg;

    /**
     * 0表示女，1表示男
     */
    private Integer sex;

    /**
     * 积分
     */
    private Integer points;

    /**
     * 邮箱
     */
    private String mail;
}