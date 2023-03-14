package hygge.logging.util.definition;

import hygge.logging.configuration.base.HyggeLogConfiguration;
import hygge.logging.enums.OutputModeEnum;
import hygge.util.definition.HyggeUtil;

/**
 * 日志模板生成器
 *
 * @author Xavier
 * @date 2022/6/28
 * @since 1.0
 */
public interface HyggeLogPatterHelper extends HyggeUtil {
    /**
     * 创建日志格式模板
     *
     * @param actualHyggeScope     实际的是否为 hygge 范围组件
     * @param actualEnableColorful 实际的是否开启彩色日志
     * @param actualOutputMode     实际的日志输出类型
     * @return 日志格式模板
     */
    String createPatter(HyggeLogConfiguration configuration, boolean actualHyggeScope, boolean actualEnableColorful, OutputModeEnum actualOutputMode);
}
