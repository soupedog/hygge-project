package hygge.web.utils.log.annotation;

import hygge.commons.templates.core.annotation.HyggeExpressionInfo;
import hygge.web.template.HyggeControllerResponse;
import org.springframework.http.RequestEntity;

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
public @interface ControllerAutoLogConfiguration {
    /**
     * 是否激活该注解。未激活时，该注解不会产生实质性作用。
     */
    boolean enable() default true;

    /**
     * 忽略的入参的形参名称，名单里的参数不参与序列化
     */
    String[] ignoreParamNames() default {};

    /**
     * 指明入参的取值表达式，你可以自由地序列化部分入参</br>
     * 假定暴露端点函数为：函数为 ResponseEntity<HyggeControllerResponse<User>> save(User user)</br>
     * 那么：["user.age","user.name"]</br>
     * 序列化入参时，如有名为 "user" 的入参，那么仅另其 "age"、"name" 参数参与序列化
     *
     * @see RequestEntity,HyggeControllerResponse
     */
    HyggeExpressionInfo[] inputParamGetExpressions() default {};

    /**
     * 指明出参的取值表达式，你可以自由地序列化部分出参。语法为 "SpEL"，你可以使用 "#root" 代替原始的出参</br>
     * 假定暴露端点函数为： ResponseEntity<DefaultResponse<User>> save(User user)</br>
     * 那么： ["main.age","main.name"]  </br>
     * 序列化出参时，那么仅令出参 User 对象的 "age"、"name" 属性参与序列化
     *
     * @see RequestEntity,HyggeControllerResponse
     */
    HyggeExpressionInfo[] outputParamExpressions() default {};
}
