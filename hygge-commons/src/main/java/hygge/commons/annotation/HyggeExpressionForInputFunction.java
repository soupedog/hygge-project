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

package hygge.commons.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于为函数入参收集表达式信息的注解
 *
 * @author Xavier
 * @date 2022/7/14
 * @since 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface HyggeExpressionForInputFunction {
    /**
     * 当前表达式是否启用。
     */
    boolean enable() default true;

    /**
     * 函数入参名称，用于指定若干个入参中具体哪个对象是表达式的执行者
     */
    String rootObjectName();

    /**
     * {@link HyggeExpressionForInputFunction#value()} 执行表达式获取到的结果的名称
     */
    String name();

    /**
     * 表达式具体内容，语法为 "SpEL"
     */
    String value();
}
