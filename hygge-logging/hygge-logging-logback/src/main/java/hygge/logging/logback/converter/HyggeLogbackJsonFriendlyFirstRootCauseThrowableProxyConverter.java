package hygge.logging.logback.converter;

import ch.qos.logback.classic.pattern.RootCauseFirstThrowableProxyConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import hygge.logging.enums.ConverterModeEnum;
import hygge.logging.utils.LogConverter;

/**
 * logback json 友好型转换工具
 *
 * @author Xavier
 * @date 2022/7/1
 * @see ConverterModeEnum ConverterKey 来源
 * @since 1.0
 */
@SuppressWarnings("java:S110")
public class HyggeLogbackJsonFriendlyFirstRootCauseThrowableProxyConverter extends RootCauseFirstThrowableProxyConverter {
    @Override
    public String convert(ILoggingEvent event) {
        return LogConverter.INSTANCE.jsonFriendlyConverter(super.convert(event));
    }
}