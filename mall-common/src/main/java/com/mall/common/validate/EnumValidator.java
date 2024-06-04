package com.mall.common.validate;

import com.mall.common.enums.BaseEnum;
import com.mall.common.utils.ArrayUtils;
import com.mall.common.validate.annotations.EnumValid;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

/**
 * @author liming
 * @version 1.0
 * @since 2024/4/3 14:51
 */
@Slf4j
public class EnumValidator implements ConstraintValidator<EnumValid, BaseEnum> {
    private int[] enums = null;

    @Override
    public void initialize(EnumValid enumValid) {
        this.enums = enumValid.enumeration();
        log.info("payload >> {}", ArrayUtils.toString(enumValid.payload()));
    }

    @Override
    public boolean isValid(BaseEnum em, ConstraintValidatorContext context) {
        // 不做空校验
        if (em == null) {
            return true;
        }
        //没有配置枚举值不校验
        if (ArrayUtils.isEmpty(enums)) {
            return true;
        }
        for (int e : enums) {
            if (e == em.getValue()) {
                return true;
            }
        }
        return false;
    }
}
