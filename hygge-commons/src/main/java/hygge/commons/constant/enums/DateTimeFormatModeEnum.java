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

package hygge.commons.constant.enums;

import hygge.commons.template.definition.DateTimeFormatMode;

import java.time.format.DateTimeFormatter;

/**
 * 日期格式化模式枚举
 *
 * @author Xavier
 * @date 2022/6/25
 * @since 1.0
 */
public enum DateTimeFormatModeEnum implements DateTimeFormatMode {
    /**
     * 样例： 2020-12-3
     */
    DATE("yyyy-MM-dd", DateTimeFormatter.ofPattern("yyyy-MM-dd"), false),
    /**
     * 样例： 04:18:28
     */
    TIME("HH:mm:ss", DateTimeFormatter.ofPattern("HH:mm:ss"), false),
    /**
     * 样例： 2020-12-3 04:18:28
     */
    DEFAULT("yyyy-MM-dd HH:mm:ss", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"), false),
    /**
     * 样例： 2020123041828
     */
    DEFAULT_TRIM("yyyyMMddHHmmss", DateTimeFormatter.ofPattern("yyyyMMddHHmmss"), false),
    /**
     * 样例： 2020123041828000
     */
    FULL_TRIM("yyyyMMddHHmmssSSS", DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"), false),
    /**
     * 样例： 2021-08-22T22:22:05.000+0800
     */
    DEFAULT_ZONE("yyyy-MM-dd'T'HH:mm:ss.SSSZ", DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ"), true),
    ;

    private final String pattern;
    private final DateTimeFormatter dateTimeFormatter;
    private final boolean withTimeZone;

    DateTimeFormatModeEnum(String pattern, DateTimeFormatter dateTimeFormatter, boolean withTimeZone) {
        this.pattern = pattern;
        this.dateTimeFormatter = dateTimeFormatter;
        this.withTimeZone = withTimeZone;
    }

    public String getPattern() {
        return pattern;
    }

    public DateTimeFormatter getDateTimeFormatter() {
        return dateTimeFormatter;
    }

    @Override
    public boolean withTimeZone() {
        return withTimeZone;
    }
}