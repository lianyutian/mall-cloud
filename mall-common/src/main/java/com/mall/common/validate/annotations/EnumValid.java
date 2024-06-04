package com.mall.common.validate.annotations;

import com.mall.common.validate.EnumValidator;
import com.mall.common.validate.EnumValueValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * 用于状态的枚举校验
 *
 * @author liming
 * @version 1.0
 * @since 2024/4/3 14:50
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.PARAMETER, ElementType.FIELD, ElementType.METHOD })
@Constraint(validatedBy = {EnumValidator.class, EnumValueValidator.class})
public @interface EnumValid {
    String message() default "不满足业务条件";

    int[] enumeration() default {};

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
