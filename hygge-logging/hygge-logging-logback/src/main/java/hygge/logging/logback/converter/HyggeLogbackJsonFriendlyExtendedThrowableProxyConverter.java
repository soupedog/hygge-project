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

package hygge.logging.logback.converter;

import ch.qos.logback.classic.pattern.ExtendedThrowableProxyConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import hygge.logging.enums.ConverterModeEnum;
import hygge.logging.util.LogConverter;

/**
 * logback json 友好型转换工具
 *
 * @author Xavier
 * @date 2022/7/1
 * @see ConverterModeEnum ConverterKey 来源
 * @since 1.0
 */
public class HyggeLogbackJsonFriendlyExtendedThrowableProxyConverter extends ExtendedThrowableProxyConverter {
    @Override
    public String convert(ILoggingEvent event) {
        return LogConverter.INSTANCE.jsonFriendlyConverter(super.convert(event));
    }
}
