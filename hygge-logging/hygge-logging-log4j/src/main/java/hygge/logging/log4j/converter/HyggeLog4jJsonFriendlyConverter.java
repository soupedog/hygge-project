package hygge.logging.log4j.converter;

import hygge.commons.exceptions.UtilRuntimeException;
import hygge.logging.enums.ConverterModeEnum;
import hygge.logging.utils.LogConverter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.core.pattern.ConverterKeys;
import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;
import org.apache.logging.log4j.core.pattern.PatternConverter;
import org.apache.logging.log4j.core.pattern.PatternFormatter;
import org.apache.logging.log4j.core.pattern.PatternParser;

import java.util.List;

/**
 * log4j json 友好型转换工具
 *
 * @author Xavier
 * @date 2022/7/1
 * @see ConverterModeEnum ConverterKey 来源
 * @since 1.0
 */
@Plugin(name = "HyggeLog4jJsonFriendlyConverter", category = PatternConverter.CATEGORY)
@ConverterKeys({"jsonString"})
public class HyggeLog4jJsonFriendlyConverter extends LogEventPatternConverter {
    private List<PatternFormatter> formatters;

    public HyggeLog4jJsonFriendlyConverter(List<PatternFormatter> formatters) {
        super("HyggeLog4jJsonFriendlyConverter", "HyggeLog4jJsonFriendlyConverter");
        this.formatters = formatters;
    }

    /**
     * @see "https://logging.apache.org/log4j/2.x/manual/plugins.html#Converters"
     */
    public static HyggeLog4jJsonFriendlyConverter newInstance(Configuration config, String[] options) {
        if (options == null || options.length < 1 || options[0] == null) {
            throw new UtilRuntimeException("Fail to newInstance of HyggeLoggerPattenJsonStringConverter,options was empty.");
        }
        PatternParser parser = PatternLayout.createPatternParser(config);
        List<PatternFormatter> formatters = parser.parse(options[0]);
        return new HyggeLog4jJsonFriendlyConverter(formatters);
    }

    @Override
    public void format(LogEvent event, StringBuilder toAppendTo) {
        StringBuilder stringBuilder = new StringBuilder();
        for (PatternFormatter formatter : this.formatters) {
            formatter.format(event, stringBuilder);
        }
        if (stringBuilder.length() > 0) {
            toAppendTo.append(LogConverter.INSTANCE.jsonFriendlyConverter(stringBuilder));
        }
    }

    /**
     * 此 Patten 进行异常处理，需终止后续异常常规流程
     */
    @Override
    public boolean handlesThrowable() {
        return true;
    }
}
