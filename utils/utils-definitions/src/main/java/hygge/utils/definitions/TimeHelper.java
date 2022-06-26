package hygge.utils.definitions;


import hygge.commons.enums.DateTimeFormatModeEnum;
import hygge.utils.HyggeUtil;

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
     * @param target                 被转化的默认时区字符串时间戳
     * @param dateTimeFormatModeEnum 格式化目标格式
     * @return UTC 毫秒级时间戳
     */
    long parse(String target, DateTimeFormatModeEnum dateTimeFormatModeEnum);

    /**
     * 特定时区字符串转化为 long 型 UTC 毫秒级时间戳
     *
     * @param target                 被转化的特定时区字符串时间戳
     * @param dateTimeFormatModeEnum 格式化目标格式
     * @param targetZoneOffset       被转化的字符串时间戳所在时区
     * @return UTC 毫秒级时间戳
     */
    long parse(String target, DateTimeFormatModeEnum dateTimeFormatModeEnum, ZoneOffset targetZoneOffset);

    /**
     * long 型 UTC 毫秒级时间戳 转化为默认时区字符串形式
     *
     * @param target                 需要被格式化的 UTC 毫秒级时间戳
     * @param dateTimeFormatModeEnum 格式化目标格式
     * @return 格式化后的字符串
     */
    String format(Long target, DateTimeFormatModeEnum dateTimeFormatModeEnum);

    /**
     * long 型 UTC 毫秒级时间戳 转化为特定时区字符串形式
     *
     * @param target                 需要被格式化的 UTC 毫秒级时间戳
     * @param dateTimeFormatModeEnum 格式化目标格式
     * @param resultZoneOffset       结果字符串对应时区
     * @return 格式化后的字符串
     */
    String format(Long target, DateTimeFormatModeEnum dateTimeFormatModeEnum, ZoneOffset resultZoneOffset);
}
