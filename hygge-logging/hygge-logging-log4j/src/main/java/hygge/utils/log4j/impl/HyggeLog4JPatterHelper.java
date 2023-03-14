package hygge.utils.log4j.impl;

import hygge.commons.exception.UtilRuntimeException;
import hygge.logging.configuration.HyggeLogConfiguration;
import hygge.logging.enums.OutputModeEnum;
import hygge.utils.definitions.HyggeLogPatterHelper;

/**
 * 基于 log4j 的 LogPatterHelper 默认实现
 *
 * @author Xavier
 * @date 2022/6/28
 * @since 1.0
 */
public class HyggeLog4JPatterHelper implements HyggeLogPatterHelper {
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
    public String createPatter(HyggeLogConfiguration configuration, boolean actualHyggeScope, boolean actualEnableColorful, OutputModeEnum actualOutputMode) {
        if (actualOutputMode.equals(OutputModeEnum.CONSOLE_AND_FILE)) {
            // 生成阶段不允许指定组合模式
            throw new UtilRuntimeException(String.format("OutputMode(%s) is not allowed in the final creation phase.", actualOutputMode));
        }

        String finalPatter;

        if (configuration.isEnableJsonType()) {
            String type = actualHyggeScope ? "hygge" : "root";

            HyggeLog4jJsonPatterHelper hyggeLog4jJsonPatterHelper = new HyggeLog4jJsonPatterHelper(
                    type,
                    configuration.getProjectName(),
                    configuration.getAppName(),
                    configuration.getVersion()
            );
            finalPatter = hyggeLog4jJsonPatterHelper.create(actualEnableColorful, configuration.getConverterMode()) + "%n";
        } else {
            // 非 json 类型
            if (actualEnableColorful) {
                finalPatter = actualHyggeScope ? HYGGE_DEFAULT_COLORFUL_PATTERN : ROOT_DEFAULT_COLORFUL_PATTERN;
            } else {
                finalPatter = actualHyggeScope ? HYGGE_DEFAULT_PATTERN : ROOT_DEFAULT_PATTERN;
            }
        }

        return finalPatter;
    }
}
