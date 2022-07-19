package hygge.commons.templates.configuration.inner;

import hygge.commons.templates.container.base.AbstractHyggeKeeper;

import java.util.Map;

/**
 * hygge 配置项
 *
 * @author Xavier
 * @date 2022/6/25
 * @since 1.0
 */
public class HyggeConfigurationItemKeeper extends AbstractHyggeKeeper<String, HyggeConfigurationItem<?>> {
    public HyggeConfigurationItemKeeper() {
    }

    public HyggeConfigurationItemKeeper(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }

    public Map<String, HyggeConfigurationItem<?>> getContainer() {
        return container;
    }

    public void setContainer(Map<String, HyggeConfigurationItem<?>> container) {
        this.container = container;
    }
}
