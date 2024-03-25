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

package hygge.commons.constant.enums;

import hygge.commons.template.definition.HyggeCode;

/**
 * 通用型全局业务码
 *
 * @author Xavier
 * @date 2022/6/25
 * @since 1.0
 */
public enum GlobalHyggeCodeEnum implements HyggeCode {
    /**
     * 响应正常
     */
    SUCCESS(true, false, null, 200, null),
    /**
     * 默认的服务端引发异常
     */
    SERVER_END_EXCEPTION(true, true, "Internal Server Error", 500, null),
    /**
     * 默认的工具类引发异常
     */
    UTIL_EXCEPTION(true, true, "Internal Server Error", 500, null),
    /**
     * 默认的依赖的远端系统引发异常
     */
    EXTERNAL_SYSTEM_EXCEPTION(true, true, "External Server Error", 500, null),
    /**
     * 默认的客户端端引发异常
     */
    CLIENT_END_EXCEPTION(true, false, null, 400, null),
    /**
     * 默认的不符合预期的入参入参引发异常
     */
    UNEXPECTED_PARAMETER(true, false, null, 400, null),
    ;

    private boolean codeDuplicateEnable;
    private final boolean serious;
    private final String publicMessage;
    private Object code;
    private Object extraInfo;

    @Override
    public boolean isCodeDuplicateEnable() {
        return codeDuplicateEnable;
    }

    @Override
    public boolean isSerious() {
        return serious;
    }

    @Override
    public String getPublicMessage() {
        return publicMessage;
    }

    @Override
    public <C> C getCode() {
        return (C) code;
    }

    @Override
    public <E> E getExtraInfo() {
        return (E) extraInfo;
    }

    GlobalHyggeCodeEnum(boolean codeDuplicateEnable, boolean serious, String publicMessage, Object code, Object extraInfo) {
        this.codeDuplicateEnable = codeDuplicateEnable;
        this.serious = serious;
        this.publicMessage = publicMessage;
        this.code = code;
        this.extraInfo = extraInfo;
    }

    public void setCodeDuplicateEnable(boolean codeDuplicateEnable) {
        this.codeDuplicateEnable = codeDuplicateEnable;
    }

    public void setCode(Object code) {
        this.code = code;
    }

    public void setExtraInfo(Object extraInfo) {
        this.extraInfo = extraInfo;
    }
}
