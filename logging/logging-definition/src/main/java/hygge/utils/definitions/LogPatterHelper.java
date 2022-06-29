package hygge.utils.definitions;

import hygge.logging.configuration.HyggeLogConfiguration;
import hygge.logging.enums.ConverterModeEnum;
import hygge.utils.HyggeUtil;

/**
 * 日志模板生成器
 *
 * @author Xavier
 * @date 2022/6/28
 * @since 1.0
 */
public interface LogPatterHelper extends HyggeUtil {
    /**
     * 创建日志格式模板
     *
     * @param hyggeScope     是否为 hygge 范围组件
     * @param enableColorful 日志是否启用彩色
     * @param enableJsonType 每条日志整体作为 json 字符串
     * @param converterMode  日志内容转化方式
     * @return 日志格式模板
     */
    String createPatter(HyggeLogConfiguration configuration, boolean hyggeScope, boolean enableColorful, boolean enableJsonType, ConverterModeEnum converterMode);
}
