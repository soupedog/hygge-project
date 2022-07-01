package hygge.utils.definitions;

import hygge.logging.configuration.HyggeLogConfiguration;
import hygge.utils.HyggeUtil;

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
     * @param hyggeScope 是否为 hygge 范围组件
     * @return 日志格式模板
     */
    String createPatter(HyggeLogConfiguration configuration, boolean hyggeScope);
}
