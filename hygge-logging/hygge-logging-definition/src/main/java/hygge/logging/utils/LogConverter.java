package hygge.logging.utils;

import hygge.commons.exception.UtilRuntimeException;
import org.apache.commons.text.translate.AggregateTranslator;
import org.apache.commons.text.translate.LookupTranslator;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 日志内容转换器
 *
 * @author Xavier
 * @date 2022/6/28
 * @since 1.0
 */
public enum LogConverter {
    INSTANCE;

    private static final AggregateTranslator JSON_FRIENDLY_TRANSLATOR;

    /**
     * 将输入内容里的
     * <pre>
     *     "
     *     \
     *     \t
     * </pre>
     * 进行 URL 编码后输出
     */
    public String jsonFriendlyConverter(CharSequence raw) {
        return JSON_FRIENDLY_TRANSLATOR.translate(raw);
    }

    static {
        final Map<CharSequence, CharSequence> jsonStringLookupMap = new HashMap<>();

        try {
            jsonStringLookupMap.put("\"", URLEncoder.encode("\"", StandardCharsets.UTF_8.displayName()));
            jsonStringLookupMap.put("\\", URLEncoder.encode("\\", StandardCharsets.UTF_8.displayName()));
            jsonStringLookupMap.put("\t", URLEncoder.encode("\t", StandardCharsets.UTF_8.displayName()));
            jsonStringLookupMap.put(System.lineSeparator(), URLEncoder.encode(System.lineSeparator(), StandardCharsets.UTF_8.displayName()));
        } catch (UnsupportedEncodingException e) {
            throw new UtilRuntimeException("LogConverter fail to init.", e);
        }

        JSON_FRIENDLY_TRANSLATOR = new AggregateTranslator(new LookupTranslator(Collections.unmodifiableMap(jsonStringLookupMap)));
    }
}
