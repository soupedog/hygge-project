package hygge.commons.templates.core.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于收集表达式信息的注解
 *
 * @author Xavier
 * @date 2022/7/14
 * @since 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface HyggeExpressionInfo {
    /**
     * 是否激活该注解。未激活时，该注解不会产生实质性作用。
     */
    boolean enable() default true;

    /**
     * 需要执行表达式的目标对象名称
     */
    String rootObjectName();

    /**
     * {@link HyggeExpressionInfo#value()} 执行表达式获取到的结果的名称
     */
    String name();

    /**
     * 表达式具体内容
     */
    String value();
}
