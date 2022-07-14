package hygge.web.utils.log.annotation;

import hygge.commons.templates.core.annotation.HyggeExpressionInfo;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Controller 层自动日志配置注解
 *
 * @author Xavier
 * @date 2022/7/14
 * @since 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ControllerAutoLog {
    /**
     * 是否激活该注解。未激活时，该注解不会产生实质性作用。
     */
    boolean enable() default true;

    /**
     * 忽略的入参的形参名称，名单里的参数不参与序列化</br>
     * 优先级高于表达式，意味着如果入参被忽略，与其相关的表达式也将直接失效
     */
    String[] ignoreParamNames() default {};

    /**
     * 指明入参的取值表达式，你可以自由地序列化部分入参
     */
    HyggeExpressionInfo[] inputParamGetExpressions() default {};

    /**
     * 指明出参的取值表达式，你可以自由地序列化部分出参。语法为 "SpEL"，你可以使用 "#root" 代替原始的出参
     */
    HyggeExpressionInfo[] outputParamExpressions() default {};
}
