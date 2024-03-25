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

import hygge.commons.annotation.HyggeExpressionInfo;

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
     * 被当前注解标记的方法是否激活 Controller 层日志。
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
