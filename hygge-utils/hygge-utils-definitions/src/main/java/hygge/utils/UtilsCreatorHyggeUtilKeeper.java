package hygge.utils;

import hygge.commons.templates.container.AbstractHyggeKeeper;

/**
 * @author Xavier
 * @date 2022/6/25
 * @since 1.0
 */
public class UtilsCreatorHyggeUtilKeeper extends AbstractHyggeKeeper<String, HyggeUtil> {
    public UtilsCreatorHyggeUtilKeeper() {
    }

    public UtilsCreatorHyggeUtilKeeper(int initialCapacity) {
        super(initialCapacity);
    }
}