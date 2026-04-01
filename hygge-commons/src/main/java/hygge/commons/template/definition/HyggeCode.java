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

package hygge.commons.template.definition;

import hygge.commons.exception.main.HyggeException;
import hygge.commons.exception.main.HyggeRuntimeException;

/**
 * 业务码模板
 *
 * @author Xavier
 * @date 2022/6/25
 * @since 1.0
 */
public interface HyggeCode {
    /**
     * code 值是否允许重复，设置为 true 时，自身不参与 code 值冲突检查，不会被其他业务码作为比较对象
     *
     * @see hygge.commons.spring.validator.impl.HyggeCodeUniqueValidator#validate
     */
    default boolean isCodeDuplicateEnable() {
        return false;
    }

    /**
     * 当前业务码对应的情景是否发生了严重问题。
     * <br/>
     * <br/>
     * 返回 false 通常代表：①无异常，正常流程、②预期之内的情景，请求方通过调整请求参数重试即可自愈<br/>
     * 返回 true 通常代表：需要人工介入检查，通常出现了无法自愈问题
     */
    boolean isSerious();

    /**
     * 获取已脱敏可暴露给系统外部请求发起者的提示信息
     */
    String getPublicMessage();

    /**
     * 获取业务标识码
     */
    <C> C getCode();

    /**
     * 用于自由拓展的额外的信息，你可以通过该属性向 HyggeCode 的接收者传递某些信息
     *
     * @see HyggeException
     * @see HyggeRuntimeException
     * @see hygge.web.template.definition.HyggeBaseController
     */
    <E> E getExtraInfo();
}
