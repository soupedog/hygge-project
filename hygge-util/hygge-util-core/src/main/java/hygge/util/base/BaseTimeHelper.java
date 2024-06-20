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
import java.time.ZoneId;
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
     * 默认时区<br/>
     * 该工具在遇到未指明时间戳时区时所用的默认值
     */
    protected ZoneId defaultZone;
    /**
     * 参数处理工具
     */
    protected ParameterHelper parameterHelper = UtilCreator.INSTANCE.getDefaultInstance(ParameterHelper.class);

    protected BaseTimeHelper() {
        initZone();
    }

    /**
     * 初始化默认时区
     */
    public abstract void initZone();

    @Override
    public long parse(String stringTimestamp, DateTimeFormatMode dateTimeFormatMode) {
        return parse(stringTimestamp, dateTimeFormatMode, defaultZone);
    }

    @Override
    public long parse(String stringTimestamp, DateTimeFormatMode dateTimeFormatMode, ZoneId zone) {
        parameterHelper.objectNotNull("stringTimestamp", (Object) stringTimestamp);
        parameterHelper.objectNotNull("dateTimeFormatMode", dateTimeFormatMode);
        parameterHelper.objectNotNull("zone", zone);

        try {
            long result;
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
                result = ZonedDateTime.of(targetLocalTime, zone).toEpochSecond();
            }
            return result;
        } catch (DateTimeParseException exception) {
            throw new ParameterRuntimeException("Fail to parse " + stringTimestamp + " to " + dateTimeFormatMode.getPattern() + ".", exception);
        }
    }

    @Override
    public String format(Long utcMillisecond, DateTimeFormatMode dateTimeFormatMode) {
        return format(utcMillisecond, dateTimeFormatMode, defaultZone);
    }

    @Override
    public String format(Long utcMillisecond, DateTimeFormatMode dateTimeFormatMode, ZoneId zone) {
        parameterHelper.objectNotNull("utcMillisecond", utcMillisecond);
        parameterHelper.objectNotNull("dateTimeFormatModeEnum", dateTimeFormatMode);
        parameterHelper.objectNotNull("zone", zone);

        try {
            ZonedDateTime targetTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(utcMillisecond), zone);
            return targetTime.format(dateTimeFormatMode.getDateTimeFormatter());
        } catch (DateTimeException exception) {
            throw new ParameterRuntimeException("Fail to format " + utcMillisecond + " to " + dateTimeFormatMode.getPattern() + ".", exception);
        }
    }
}
