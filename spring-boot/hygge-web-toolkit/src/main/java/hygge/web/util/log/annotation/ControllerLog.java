/*
 * Copyright 2022-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package hygge.web.util.log.annotation;

import hygge.commons.annotation.HyggeExpressionForInputFunction;
import hygge.commons.annotation.HyggeExpressionForOutputFunction;
import hygge.web.util.log.ControllerLogContext;
import hygge.web.util.log.base.BaseControllerLogHandler;
import org.aopalliance.intercept.MethodInvocation;

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
public @interface ControllerLog {
    /**
     * 指定日志是否进行记录。
     * <p>
     * 该值为 {@link Boolean#FALSE} 时也仍会执行  {@link BaseControllerLogHandler#hook(ControllerLogContext, MethodInvocation)}
     */
    boolean logRecordEnable() default true;

    /**
     * 忽略的入参的形参名称，名单里的参数不参与日志记录</br>
     * 优先级高于表达式，意味着如果入参被忽略，与其相关的表达式也将直接失效
     */
    String[] ignoreParamNames() default {};

    /**
     * 是否需要记录入参(方便切换到仅做统计，不关心出入参的场景)
     * <p>
     * 该值为 {@link Boolean#FALSE} 时，取值表达式将失效，日志将忽略入参的记录
     */
    boolean inputParamEnable() default true;

    /**
     * 指明入参的取值表达式，你可以自由地序列化部分入参，语法为 "SpEL"
     */
    HyggeExpressionForInputFunction[] inputParamGetExpressions() default {};

    /**
     * 是否需要记录出参(方便切换到仅做统计，不关心出入参的场景)
     * <p>
     * 该值为 {@link Boolean#FALSE} 时，取值表达式将失效，日志将忽略出参的记录
     */
    boolean outputParamEnable() default true;

    /**
     * 指明出参的取值表达式，你可以自由地序列化部分出参，语法为 "SpEL"
     */
    HyggeExpressionForOutputFunction[] outputParamExpressions() default {};
}
