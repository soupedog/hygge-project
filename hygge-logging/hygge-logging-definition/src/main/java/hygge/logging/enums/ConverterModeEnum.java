package hygge.logging.enums;

import hygge.logging.utils.LogConverter;
import org.apache.commons.text.StringEscapeUtils;

/**
 * 日志内容转换模式，在日志最终输出前，对日志内容进行额外处理
 *
 * @author Xavier
 * @date 2022/6/28
 * @since 1.0
 */
public enum ConverterModeEnum {
    /**
     * 不进行额外处理，原样输出
     */
    DEFAULT("DEFAULT", null),
    /**
     * 日志信息进行 escape 编码后输出
     *
     * @see StringEscapeUtils#escapeJava(String)
     */
    ESCAPE("ESCAPE", "escape"),
    /**
     * 将原内容中的 {@code "} 和 {@code /} 进行转译后输出<br/>
     * json 字符串友好型，防止嵌套层内容破坏外层 json 数据结构
     *
     * @see LogConverter#jsonFriendlyConverter(CharSequence)
     */
    JSON_FRIENDLY("JSON_STRING", "jsonString");

    ConverterModeEnum(String description, String converterKey) {
        this.description = description;
        this.converterKey = converterKey;
    }

    /**
     * 描述
     */
    private final String description;
    /**
     * converterKey
     */
    private final String converterKey;

    public String getDescription() {
        return description;
    }

    public String getConverterKey() {
        return converterKey;
    }
}
