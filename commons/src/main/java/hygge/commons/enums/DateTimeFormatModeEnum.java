package hygge.commons.enums;

import java.time.format.DateTimeFormatter;

/**
 * 日期格式化模式
 *
 * @author Xavier
 * @date 2022/6/25
 * @since 1.0
 */
public enum DateTimeFormatModeEnum {
    /**
     * 样例： 2020-12-3
     */
    DATE("yyyy-MM-dd", DateTimeFormatter.ofPattern("yyyy-MM-dd")),
    /**
     * 样例： 04:18:28
     */
    TIME("HH:mm:ss", DateTimeFormatter.ofPattern("HH:mm:ss")),
    /**
     * 样例： 2020-12-3 04:18:28
     */
    DEFAULT("yyyy-MM-dd HH:mm:ss", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
    /**
     * 样例： 2021-08-22T22:22:05.000+0800
     */
    DEFAULT_ZONE("yyyy-MM-dd'T'HH:mm:ss.SSSZ", DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ")),
    /**
     * 样例： 2020123041828
     */
    DEFAULT_TRIM("yyyyMMddHHmmss", DateTimeFormatter.ofPattern("yyyyMMddHHmmss")),
    /**
     * 样例： 2020123041828000
     */
    FULL_TRIM("yyyyMMddHHmmssSSS", DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));

    private final String pattern;
    private final DateTimeFormatter dateTimeFormatter;

    DateTimeFormatModeEnum(String pattern, DateTimeFormatter dateTimeFormatter) {
        this.pattern = pattern;
        this.dateTimeFormatter = dateTimeFormatter;
    }

    public String getPattern() {
        return pattern;
    }

    public DateTimeFormatter getDateTimeFormatter() {
        return dateTimeFormatter;
    }
}
