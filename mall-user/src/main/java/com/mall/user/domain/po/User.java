package com.mall.user.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.mall.common.domain.dto.BasePO;
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
@TableName("user")
public class User extends BasePO implements Serializable {
    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
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