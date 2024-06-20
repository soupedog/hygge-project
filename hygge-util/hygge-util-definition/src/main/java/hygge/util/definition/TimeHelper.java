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

package hygge.util.definition;


import hygge.commons.exception.ParameterRuntimeException;
import hygge.commons.template.definition.DateTimeFormatMode;

import java.time.ZoneId;
import java.time.format.DateTimeParseException;


/**
 * 时间处理工具
 *
 * @author Xavier
 * @date 2022/6/25
 * @since 1.0
 */
public interface TimeHelper extends HyggeUtil {
    /**
     * 将字符串时间戳转化为 long 型 UTC 毫秒级时间戳
     *
     * @param stringTimestamp    将被转化为 long 型 UTC 毫秒级时间戳的字符串时间戳
     * @param dateTimeFormatMode 字符串时间戳的格式信息。如果 {@link DateTimeFormatMode#withTimeZone()} 返回 {@link Boolean#FALSE}，传入的字符串时间戳会被认定为来自默认时区
     * @return UTC 毫秒级时间戳
     * @throws ParameterRuntimeException 如果时间转换过程中遇到 {@link DateTimeParseException}，会将其二次封装为 {@link ParameterRuntimeException} 重新抛出
     */
    long parse(String stringTimestamp, DateTimeFormatMode dateTimeFormatMode);

    /**
     * 将字符串时间戳转化为 long 型 UTC 毫秒级时间戳
     *
     * @param stringTimestamp    将被转化为 long 型 UTC 毫秒级时间戳的字符串时间戳
     * @param dateTimeFormatMode 字符串时间戳的格式信息。如果 {@link DateTimeFormatMode#withTimeZone()} 返回 {@link Boolean#FALSE}，传入的字符串时间戳会被认定为来自指定的 zone
     * @param zone               用于描述无时区字符串时间戳的时区。{@link DateTimeFormatMode#withTimeZone()} 返回 {@link Boolean#TRUE} 时，该参数无任何作用
     * @return UTC 毫秒级时间戳
     * @throws ParameterRuntimeException 如果时间转换过程中遇到 {@link DateTimeParseException}，会将其二次封装为 {@link ParameterRuntimeException} 重新抛出
     */
    long parse(String stringTimestamp, DateTimeFormatMode dateTimeFormatMode, ZoneId zone);

    /**
     * 将 long 型 UTC 毫秒级时间戳转化为指定格式的字符串时间戳
     *
     * @param utcMillisecond     将被转化为字符串时间戳的 long 型 UTC 毫秒级时间戳
     * @param dateTimeFormatMode 字符串时间戳的格式信息。如果 {@link DateTimeFormatMode#withTimeZone()} 返回 {@link Boolean#FALSE}，返回结果的字符串时间戳会调整为默认时区
     * @return 所指定格式的字符串时间戳
     * @throws ParameterRuntimeException 如果时间转换过程中遇到 {@link DateTimeParseException}，会将其二次封装为 {@link ParameterRuntimeException} 重新抛出
     */
    String format(Long utcMillisecond, DateTimeFormatMode dateTimeFormatMode);

    /**
     * 将 long 型 UTC 毫秒级时间戳转化为指定格式的字符串时间戳
     *
     * @param utcMillisecond     将被转化为字符串时间戳的 long 型 UTC 毫秒级时间戳
     * @param dateTimeFormatMode 字符串时间戳的格式信息。如果 {@link DateTimeFormatMode#withTimeZone()} 返回 {@link Boolean#FALSE}，返回结果的字符串时间戳会调整为指定的 zone
     * @param zone               用于描述无时区字符串时间戳的时区。{@link DateTimeFormatMode#withTimeZone()} 返回 {@link Boolean#TRUE} 时，该参数无任何作用
     * @return 所指定格式的字符串时间戳
     * @throws ParameterRuntimeException 如果时间转换过程中遇到 {@link DateTimeParseException}，会将其二次封装为 {@link ParameterRuntimeException} 重新抛出
     */
    String format(Long utcMillisecond, DateTimeFormatMode dateTimeFormatMode, ZoneId zone);
}
