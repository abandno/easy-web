package com.nisus.baotool.util.valid;

import cn.hutool.core.util.StrUtil;
import com.nisus.baotool.core.consts.StringPool;
import com.nisus.baotool.core.exception.ArgumentException;
import com.nisus.baotool.core.exception.Exceptions;
import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * hibernate validation 校验
 * <p>
 * 嵌套校验: 字段是对象, 如果需要嵌套校验, 对象字段上加上注解 `@Valid`
 * @author L&J
 * @date 2021/10/15 5:26 下午
 */
public class ValidateUtil {

    private static Validator validator;
    static {
        ValidatorFactory validatorFactory = Validation.byProvider(HibernateValidator.class)
                .configure()
                .messageInterpolator(new ParameterMessageInterpolator()) // fix: javax.validation.ValidationException: HV000183: Unable to initialize 'javax.el.ExpressionFactory'. Check that you have the EL dependencies on the classpath, or use ParameterMessageInterpolator instead
                .buildValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    public static <T> boolean validateQuietly(T object) {
        try {
            validate(object);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 校验
     *
     * 失败后, 默认抛异常 BadRequestException
     * @param object 校验目标对象
     * @param <T> 目标对象类型
     */
    public static <T> void validate(T object) {
        validate(object, null);
    }

    /**
     * 简化自定义 error supplier
     * @param object 校验目标对象
     * @param errorSupplier BiFunction&lt;List&lt;String>, Set&lt;ConstraintViolation&lt;T>>>, p1: 提取好的间接消息, p2: 原始校验结果, 非空.
     * @param <T> 目标对象类型
     */
    public static <T, X extends Throwable> void validate(T object, BiFunction<List<String>, Set<ConstraintViolation<T>>, X> errorSupplier) {
        Set<ConstraintViolation<T>> validateResult = validator.validate(object);
        if (!validateResult.isEmpty()) {
            List<String> errors = getMessage(validateResult);
            if (errorSupplier != null) {
                Exceptions.sneakyThrow(errorSupplier.apply(errors, validateResult));
            } else {
                // 默认抛异常
                throw new ArgumentException(StrUtil.join("; ", errors)).setErrors(errors);
            }
        }

        // 存在循环调用的风险！！
        // if (object instanceof Validable) {
        //     // !!一般建议自己在 validate 里抛出异常, 错误信息输出更灵活丰富
        //     if (!((Validable) object).validate()) {
        //         throw new BadRequestException("Validable.validate 不通过");
        //     }
        // }
    }

    // /**
    //  * @param object 校验目标对象
    //  * @param afterInvalid BiConsumer&lt;List&lt;String>, Set&lt;ConstraintViolation&lt;T>>>, p1: 提取好的间接消息, p2: 原始校验结果, 非空.
    //  * @param <T> 目标对象类型
    //  */
    // public static <T> void validate(T object, BiConsumer<List<String>, Set<ConstraintViolation<T>>> afterInvalid) {
    //     Set<ConstraintViolation<T>> validateResult = validator.validate(object);
    //     if (!validateResult.isEmpty()) {
    //         List<String> errors = getMessage(validateResult);
    //         if (afterInvalid != null) {
    //             afterInvalid.accept(errors, validateResult);
    //         } else {
    //             // 默认抛异常
    //             throw new BadRequestException(StrUtil.join("; ", errors)).setErrors(errors);
    //         }
    //     }
    // }

    /**
     *
     * <pre>
     * interpolatedMessage: "不能为null", // getMessage() 可获取
     * rootBean: bean,
     * propertyPath: "字段路径",
     * messageTemplate: "{javax.validation.constraints.NotNull.message}", 以此为 key 换成本地换语言输出, 即 interpolatedMessage
     * rootBeanClass: 被校验的类 class
     *
     * </pre>
     *
     * @param validateResult
     * @param <T>
     * @return
     */
    public static <T> List<String> getMessage(Set<ConstraintViolation<T>> validateResult) {
        if (validateResult == null) {
            return Collections.emptyList();
        }
        return validateResult.stream().map(it -> {
            return "'" + it.getRootBeanClass().getSimpleName() + StringPool.DOT + it.getPropertyPath() + "' " + it.getMessage();
        }).collect(Collectors.toList());
    }


}
