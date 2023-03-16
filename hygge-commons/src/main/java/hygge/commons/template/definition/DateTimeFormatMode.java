package hygge.commons.template.definition;

import java.time.format.DateTimeFormatter;

/**
 * 日期格式化模式
 *
 * @author Xavier
 * @date 2022/6/25
 * @since 1.0
 */
public interface DateTimeFormatMode {
    /**
     * 获取字符串时间戳模板
     */
    String getPattern();

    /**
     * 获取字符串时间戳模板对应的 DateTimeFormatter
     */
    DateTimeFormatter getDateTimeFormatter();

    /**
     * 当前字符串时间戳模板是否已经包含了时区信息
     */
    boolean withTimeZone();

}
