package hygge.logging.logback.converter;

import ch.qos.logback.classic.pattern.MessageConverter;
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
public class HyggeLogbackJsonFriendlyMessageConverter extends MessageConverter {
    @Override
    public String convert(ILoggingEvent event) {
        return LogConverter.INSTANCE.jsonFriendlyConverter(super.convert(event));
    }
}
