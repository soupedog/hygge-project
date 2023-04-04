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

package hygge.util.definition;


import hygge.commons.template.definition.DateTimeFormatMode;

import java.time.ZoneOffset;


/**
 * 时间处理工具
 *
 * @author Xavier
 * @date 2022/6/25
 * @since 1.0
 */
public interface TimeHelper extends HyggeUtil {
    /**
     * 默认时区字符串转化为 long 型 UTC 毫秒级时间戳
     *
     * @param target             被转化的默认时区字符串时间戳
     * @param dateTimeFormatMode 格式化目标格式
     * @return UTC 毫秒级时间戳
     */
    long parse(String target, DateTimeFormatMode dateTimeFormatMode);

    /**
     * 特定时区字符串转化为 long 型 UTC 毫秒级时间戳
     *
     * @param target             被转化的特定时区字符串时间戳
     * @param dateTimeFormatMode 格式化目标格式
     * @param targetZoneOffset   被转化的字符串时间戳所在时区
     * @return UTC 毫秒级时间戳
     */
    long parse(String target, DateTimeFormatMode dateTimeFormatMode, ZoneOffset targetZoneOffset);

    /**
     * long 型 UTC 毫秒级时间戳 转化为默认时区字符串形式
     *
     * @param target             需要被格式化的 UTC 毫秒级时间戳
     * @param dateTimeFormatMode 格式化目标格式
     * @return 格式化后的字符串
     */
    String format(Long target, DateTimeFormatMode dateTimeFormatMode);

    /**
     * long 型 UTC 毫秒级时间戳 转化为特定时区字符串形式
     *
     * @param target             需要被格式化的 UTC 毫秒级时间戳
     * @param dateTimeFormatMode 格式化目标格式
     * @param resultZoneOffset   结果字符串对应时区
     * @return 格式化后的字符串
     */
    String format(Long target, DateTimeFormatMode dateTimeFormatMode, ZoneOffset resultZoneOffset);
}
