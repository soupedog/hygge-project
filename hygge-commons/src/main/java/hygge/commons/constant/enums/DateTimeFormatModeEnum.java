package hygge.commons.constant.enums;

import hygge.commons.constant.enums.definition.DateTimeFormatMode;

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