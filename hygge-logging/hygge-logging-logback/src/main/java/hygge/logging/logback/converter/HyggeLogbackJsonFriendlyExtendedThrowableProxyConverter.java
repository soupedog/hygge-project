package hygge.logging.logback.converter;

import ch.qos.logback.classic.pattern.ExtendedThrowableProxyConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import hygge.logging.enums.ConverterModeEnum;
import hygge.logging.utils.LogConverter;
import org.apache.commons.text.StringEscapeUtils;

/**
 * logback json 友好型转换工具
 *
 * @author Xavier
 * @date 2022/7/1
 * @see ConverterModeEnum ConverterKey 来源
 * @since 1.0
 */
public class HyggeLogbackJsonFriendlyExtendedThrowableProxyConverter extends ExtendedThrowableProxyConverter {
    @Override
    public String convert(ILoggingEvent event) {
        return LogConverter.INSTANCE.jsonFriendlyConverter(super.convert(event));
    }
}
