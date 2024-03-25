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

package hygge.commons.exception;


import hygge.commons.constant.enums.GlobalHyggeCodeEnum;
import hygge.commons.template.definition.HyggeCode;
import hygge.commons.exception.main.HyggeRuntimeException;

/**
 * 入参不符合预期运行时异常
 *
 * @author Xavier
 * @date 2022/6/25
 * @since 1.0
 */
public class ParameterRuntimeException extends HyggeRuntimeException {
    public ParameterRuntimeException(String message) {
        super(message, GlobalHyggeCodeEnum.UNEXPECTED_PARAMETER);
    }

    public ParameterRuntimeException(String message, Throwable cause) {
        super(message, GlobalHyggeCodeEnum.UNEXPECTED_PARAMETER, cause);
    }

    public ParameterRuntimeException(String message, HyggeCode hyggeCode) {
        super(message, hyggeCode);
    }

    public ParameterRuntimeException(String message, HyggeCode hyggeCode, Throwable cause) {
        super(message, hyggeCode, cause);
    }

    public ParameterRuntimeException(String message, HyggeCode hyggeCode, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, hyggeCode, cause, enableSuppression, writableStackTrace);
    }
}
