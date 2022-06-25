package hygge.utils;

import hygge.commons.templates.core.WithName;

/**
 * hygge 工具 root
 *
 * @author Xavier
 * @date 2022/6/25
 */
public interface HyggeUtil extends WithName {
    @Override
    default String getHyggeName() {
        return this.getClass().getSimpleName();
    }
}