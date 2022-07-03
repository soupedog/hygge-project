package hygge.logging.logback.converter;

import ch.qos.logback.classic.pattern.DateConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

/**
 * 日志时间处理为 UTC 毫秒级时间戳
 *
 * @author Xavier
 * @date 2022/7/3
 * @since 1.0
 */
@SuppressWarnings("java:S110")
public class HyggeLogbackTimeStampConverter extends DateConverter {
    @Override
    public String convert(ILoggingEvent le) {
        return le.getTimeStamp() + "";
    }
}
