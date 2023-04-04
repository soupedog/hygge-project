/*
 * Copyright 2022-2023 the original author or authors.
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

package hygge.commons.exception.main;

import hygge.commons.template.definition.HyggeCode;
import hygge.commons.template.definition.HyggeInfo;

/**
 * Hygge 异常
 *
 * @author Xavier
 * @date 2022/6/25
 * @since 1.0
 */
public class HyggeException extends Exception implements HyggeInfo {
    protected final transient HyggeCode hyggeCode;

    public HyggeException(HyggeCode hyggeCode) {
        super(hyggeCode.getPublicMessage());
        this.hyggeCode = hyggeCode;
    }

    public HyggeException(String message, HyggeCode hyggeCode) {
        super(message);
        this.hyggeCode = hyggeCode;
    }

    public HyggeException(String message, HyggeCode hyggeCode, Throwable cause) {
        super(message, cause);
        this.hyggeCode = hyggeCode;
    }

    public HyggeException(String message, HyggeCode hyggeCode, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.hyggeCode = hyggeCode;
    }

    @Override
    public HyggeCode getHyggeCode() {
        return hyggeCode;
    }
}
