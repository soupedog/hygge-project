package hygge.utils.lg4j.impl;

import hygge.logging.configuration.HyggeLogConfiguration;
import hygge.logging.enums.ConverterModeEnum;
import hygge.utils.definitions.LogPatterHelper;

import static hygge.logging.enums.ConverterModeEnum.ESCAPE;
import static hygge.logging.enums.ConverterModeEnum.JSON_FRIENDLY;

/**
 * 基于 log4j 的 LogPatterHelper 默认实现
 *
 * @author Xavier
 * @date 2022/6/28
 * @since 1.0
 */
public class DefaultLogPatterHelper implements LogPatterHelper {
    /**
     * 默认的 hygge 日志模板
     */
    public static final String HYGGE_DEFAULT_PATTERN = "%d{yyyy-MM-dd HH:mm:ss.SSS} %5p %pid --- [%15.15t] %-40.40c{1.} : %m%n%xwEx";
    /**
     * 默认的 ROOT 日志模板
     */
    public static final String ROOT_DEFAULT_PATTERN = HYGGE_DEFAULT_PATTERN;
    /**
     * 默认的 hygge 彩色日志模板
     */
    public static final String HYGGE_DEFAULT_COLORFUL_PATTERN = "%clr{%d{yyyy-MM-dd HH:mm:ss.SSS}}{faint} %clr{%5p} %clr{%pid}{cyan} %clr{---}{faint} %clr{[%15.15t]}{faint} %clr{%-40.40c{1.}}{magenta} %clr{:}{faint} %m%n%xwEx";
    /**
     * 默认的 ROOT 彩色日志模板
     */
    public static final String ROOT_DEFAULT_COLORFUL_PATTERN = "%clr{%d{yyyy-MM-dd HH:mm:ss.SSS}}{faint} %clr{%5p} %clr{%pid}{magenta} %clr{---}{faint} %clr{[%15.15t]}{faint} %clr{%-40.40c{1.}}{cyan} %clr{:}{faint} %m%n%xwEx";

    @Override
    public String createPatter(HyggeLogConfiguration configuration, boolean hyggeScope, boolean enableColorful, boolean enableJsonType, ConverterModeEnum converterMode) {
        String finalPatter;

        if (enableJsonType) {
            // TODO json Template
            finalPatter = "TODO";
        } else {
            if (enableColorful) {
                finalPatter = hyggeScope ? HYGGE_DEFAULT_COLORFUL_PATTERN : ROOT_DEFAULT_COLORFUL_PATTERN;
            } else {
                finalPatter = hyggeScope ? HYGGE_DEFAULT_PATTERN : ROOT_DEFAULT_PATTERN;
            }
        }

        finalPatter = initConverter(converterMode, finalPatter);
        return finalPatter;
    }

    private String initConverter(ConverterModeEnum converterMode, String finalPatter) {
        switch (converterMode) {
            case ESCAPE:
                finalPatter = finalPatter
                        .replace("%m", "%" + ESCAPE.getConverterKey() + "{%m}")
                        .replace("%xwEx", "%" + ESCAPE.getConverterKey() + "{%xwEx}");
                break;
            case JSON_FRIENDLY:
                finalPatter = finalPatter
                        .replace("%m", "%" + JSON_FRIENDLY.getConverterKey() + "{%m}")
                        .replace("%xwEx", "%" + JSON_FRIENDLY.getConverterKey() + "{%xwEx}");
                break;
            default:
                // 维持原状
                break;
        }
        return finalPatter;
    }
}
