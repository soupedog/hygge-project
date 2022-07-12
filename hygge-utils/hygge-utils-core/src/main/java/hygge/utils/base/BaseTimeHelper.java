package hygge.utils.base;


import hygge.commons.enums.DateTimeFormatMode;
import hygge.commons.exceptions.ParameterRuntimeException;
import hygge.utils.UtilsCreator;
import hygge.utils.definitions.ParameterHelper;
import hygge.utils.definitions.TimeHelper;

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
    protected ParameterHelper parameterHelper = UtilsCreator.INSTANCE.getDefaultInstance(ParameterHelper.class);

    protected BaseTimeHelper() {
        initZoneOffset();
    }

    /**
     * 初始化默认时间戳偏移量
     */
    public abstract void initZoneOffset();

    @Override
    public long parse(String target, DateTimeFormatMode dateTimeFormatMode) {
        return parse(target, dateTimeFormatMode, defaultZoneOffset);
    }

    @Override
    public long parse(String target, DateTimeFormatMode dateTimeFormatMode, ZoneOffset targetZoneOffset) {
        parameterHelper.objectNotNull("target", (Object) target);
        parameterHelper.objectNotNull("dateTimeFormatMode", dateTimeFormatMode);
        parameterHelper.objectNotNull("targetZoneOffset", targetZoneOffset);

        long result;
        try {
            if (dateTimeFormatMode.withTimeZone()) {
                ZonedDateTime targetZonedDateTime = ZonedDateTime.parse(target, dateTimeFormatMode.getDateTimeFormatter());
                result = targetZonedDateTime.toInstant().toEpochMilli();
            } else {
                LocalDateTime targetLocalTime;
                if (DateTimeFormatMode.DateTimeFormatModeEnum.FULL_TRIM.equals(dateTimeFormatMode)) {
                    // Warning  parse(target, DateTimeFormatMode.FULL_TRIM, targetZoneOffset)
                    // @see "https://bugs.java.com/bugdatabase/view_bug.do?bug_id=JDK-8031085"
                    // jdk 8 存在 bug，jdk 9 以后才能正确运行携带毫秒信息的时间模板
                    // 下为兼容 java 8 的补救方式
                    String theFirstPart = target.substring(0, 14);
                    long millisecondVal = Long.parseLong(target.substring(14, 17)) * 1000000;
                    targetLocalTime = LocalDateTime.parse(theFirstPart, dateTimeFormatMode.getDateTimeFormatter());
                    targetLocalTime = targetLocalTime.plusNanos(millisecondVal);
                } else if (DateTimeFormatMode.DateTimeFormatModeEnum.DATE.equals(dateTimeFormatMode)) {
                    targetLocalTime = LocalDateTime.parse(target + " 00:00:00", dateTimeFormatMode.getDateTimeFormatter());
                } else {
                    targetLocalTime = LocalDateTime.parse(target, dateTimeFormatMode.getDateTimeFormatter());
                }
                result = targetLocalTime.toInstant(targetZoneOffset).toEpochMilli();
            }

            return result;
        } catch (DateTimeParseException exception) {
            throw new ParameterRuntimeException(
                    "Fail to parse " + target + " to " + dateTimeFormatMode.getPattern() + ".",
                    exception);
        }
    }

    @Override
    public String format(Long target, DateTimeFormatMode dateTimeFormatMode) {
        return format(target, dateTimeFormatMode, defaultZoneOffset);
    }

    @Override
    public String format(Long target, DateTimeFormatMode dateTimeFormatMode, ZoneOffset resultZoneOffset) {
        parameterHelper.objectNotNull("target", target);
        parameterHelper.objectNotNull("dateTimeFormatModeEnum", dateTimeFormatMode);
        parameterHelper.objectNotNull("targetZoneOffset", resultZoneOffset);

        try {
            ZonedDateTime targetTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(target), resultZoneOffset);
            return targetTime.format(dateTimeFormatMode.getDateTimeFormatter());
        } catch (DateTimeException exception) {
            throw new ParameterRuntimeException(
                    "Fail to format " + target + " to " + dateTimeFormatMode.getPattern() + ".",
                    exception);
        }
    }
}
