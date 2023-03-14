package hygge.util.definition;

import hygge.commons.template.definition.WithHyggeName;

/**
 * hygge 工具 root
 *
 * @author Xavier
 * @date 2022/6/25
 * @since 1.0
 */
public interface HyggeUtil extends WithHyggeName {
    @Override
    default String getHyggeName() {
        return this.getClass().getSimpleName();
    }
}
