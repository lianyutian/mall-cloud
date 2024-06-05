package com.mall.marketing.coupon.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.mall.common.enums.BaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author liming
 * @version 1.0
 * @since 2024/4/22 17:04
 */
@Getter
@AllArgsConstructor
public enum ObtainWay implements BaseEnum {
    /**
     * 领取类型
     */
    PUBLIC(1, "手动领取"),
    ISSUE(2, "发放兑换码");

    @JsonValue
    @EnumValue
    private final int value;
    private final String desc;

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static ObtainWay of(Integer value) {
        if (value == null) {
            return null;
        }
        for (ObtainWay status : values()) {
            if (status.value == value) {
                return status;
            }
        }
        return null;
    }
}

