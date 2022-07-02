package hygge.logging.logback.converter;

import ch.qos.logback.classic.pattern.ThrowableProxyConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import hygge.logging.enums.ConverterModeEnum;
import org.apache.commons.text.StringEscapeUtils;

/**
 * logback escape 转换工具
 *
 * @author Xavier
 * @date 2022/7/1
 * @see ConverterModeEnum ConverterKey 来源
 * @since 1.0
 */
public class HyggeLogbackEscapeThrowableProxyConverter extends ThrowableProxyConverter {
    @Override
    public String convert(ILoggingEvent event) {
        return StringEscapeUtils.escapeJava(super.convert(event));
    }
}
