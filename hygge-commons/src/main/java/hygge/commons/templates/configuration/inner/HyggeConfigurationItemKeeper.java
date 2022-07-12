package hygge.commons.templates.configuration.inner;

import hygge.commons.templates.container.AbstractHyggeKeeper;

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

    public HyggeConfigurationItemKeeper(int initialCapacity) {
        super(initialCapacity);
    }

    public Map<String, HyggeConfigurationItem<?>> getContainer() {
        return container;
    }

    public void setContainer(Map<String, HyggeConfigurationItem<?>> container) {
        this.container = container;
    }
}
