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

package hygge.util.base;


import hygge.commons.constant.enums.DateTimeFormatModeEnum;
import hygge.commons.exception.ParameterRuntimeException;
import hygge.commons.template.definition.DateTimeFormatMode;
import hygge.util.UtilCreator;
import hygge.util.definition.ParameterHelper;
import hygge.util.definition.TimeHelper;

import java.time.DateTimeException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;

/**
 * 时间处理工具类基类
 *
 * @author Xavier
 * @date 2022/6/26
 * @since 1.0
 */
public abstract class BaseTimeHelper implements TimeHelper {
    /**
     * 默认被转化时间戳的偏移量<br/>
     * 如果调用方法时未指定时区，将会默认用该时区信息标记字符串时间戳
     */
    protected ZoneOffset defaultZoneOffset;
    /**
     * 参数处理工具
     */
    protected ParameterHelper parameterHelper = UtilCreator.INSTANCE.getDefaultInstance(ParameterHelper.class);

    protected BaseTimeHelper() {
        initZoneOffset();
    }

    /**
     * 初始化默认时间戳偏移量
     */
    public abstract void initZoneOffset();

    @Override
    public long parse(String stringTimestamp, DateTimeFormatMode dateTimeFormatMode) {
        return parse(stringTimestamp, dateTimeFormatMode, defaultZoneOffset);
    }

    @Override
    public long parse(String stringTimestamp, DateTimeFormatMode dateTimeFormatMode, ZoneOffset stringTimestampZoneOffset) {
        parameterHelper.objectNotNull("stringTimestamp", (Object) stringTimestamp);
        parameterHelper.objectNotNull("dateTimeFormatMode", dateTimeFormatMode);
        parameterHelper.objectNotNull("stringTimestampZoneOffset", stringTimestampZoneOffset);

        long result;
        try {
            if (dateTimeFormatMode.withTimeZone()) {
                ZonedDateTime targetZonedDateTime = ZonedDateTime.parse(stringTimestamp, dateTimeFormatMode.getDateTimeFormatter());
                result = targetZonedDateTime.toInstant().toEpochMilli();
            } else {
                // 不带时区的时间对象
                LocalDateTime targetLocalTime;

                if (DateTimeFormatModeEnum.DATE.equals(dateTimeFormatMode)) {
                    targetLocalTime = LocalDateTime.parse(stringTimestamp + " 00:00:00", dateTimeFormatMode.getDateTimeFormatter());
                } else {
                    targetLocalTime = LocalDateTime.parse(stringTimestamp, dateTimeFormatMode.getDateTimeFormatter());
                }
                // 指定基于 UTC 时区的偏移量，并转化为 UTC long 型时间戳
                result = targetLocalTime.toInstant(stringTimestampZoneOffset).toEpochMilli();
            }
            return result;
        } catch (DateTimeParseException exception) {
            throw new ParameterRuntimeException(
                    "Fail to parse " + stringTimestamp + " to " + dateTimeFormatMode.getPattern() + ".",
                    exception);
        }
    }

    @Override
    public String format(Long utcMillisecond, DateTimeFormatMode dateTimeFormatMode) {
        return format(utcMillisecond, dateTimeFormatMode, defaultZoneOffset);
    }

    @Override
    public String format(Long utcMillisecond, DateTimeFormatMode dateTimeFormatMode, ZoneOffset stringTimestampZoneOffset) {
        parameterHelper.objectNotNull("utcMillisecond", utcMillisecond);
        parameterHelper.objectNotNull("dateTimeFormatModeEnum", dateTimeFormatMode);
        parameterHelper.objectNotNull("stringTimestampZoneOffset", stringTimestampZoneOffset);

        try {
            ZonedDateTime targetTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(utcMillisecond), stringTimestampZoneOffset);
            return targetTime.format(dateTimeFormatMode.getDateTimeFormatter());
        } catch (DateTimeException exception) {
            throw new ParameterRuntimeException(
                    "Fail to format " + utcMillisecond + " to " + dateTimeFormatMode.getPattern() + ".",
                    exception);
        }
    }
}
