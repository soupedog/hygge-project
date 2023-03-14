package hygge.util.inner;

import hygge.commons.template.container.base.AbstractHyggeKeeper;
import hygge.util.definition.HyggeUtil;

/**
 * @author Xavier
 * @date 2022/6/25
 * @since 1.0
 */
public class UtilsCreatorHyggeUtilKeeper extends AbstractHyggeKeeper<String, HyggeUtil> {
    public UtilsCreatorHyggeUtilKeeper() {
    }

    public UtilsCreatorHyggeUtilKeeper(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }
}
