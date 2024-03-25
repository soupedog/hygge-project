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

/**
 * 业务码
 *
 * @author Xavier
 * @date 2022/6/25
 * @since 1.0
 */
public interface HyggeCode {
    /**
     * code 值是否允许重复
     */
    default boolean isCodeDuplicateEnable() {
        return false;
    }

    /**
     * 是否为严重问题。
     * <p/>
     * 返回 false 通常代表是入参引发的问题，有可能通过重试自愈<br/>
     * 返回 true 时需要人工介入检查，通常无法自愈
     */
    boolean isSerious();

    /**
     * 获取已脱敏可暴露给系统外部的提示信息
     */
    String getPublicMessage();

    /**
     * 获取业务标识码
     */
    <C> C getCode();

    /**
     * 用于自由拓展的额外的信息，你可以通过该属性向 HyggeCode 的接收者传递某些信息
     */
    <E> E getExtraInfo();
}
