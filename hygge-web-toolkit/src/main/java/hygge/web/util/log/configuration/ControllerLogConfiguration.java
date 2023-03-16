package hygge.web.util.log.configuration;

import hygge.commons.spring.config.configuration.definition.HyggeSpringConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.Ordered;

/**
 * Controller 层自动日志配置项
 *
 * @author Xavier
 * @date 2022/7/12
 * @since 1.0
 */
@ConfigurationProperties(prefix = "hygge.web-toolkit.controller.log")
public class ControllerLogConfiguration implements HyggeSpringConfigurationProperties {
    public static final int DEFAULT_ASPECT_ORDER = Ordered.LOWEST_PRECEDENCE - 2;

    private boolean autoRegister = true;

    private int aspectOrder = DEFAULT_ASPECT_ORDER;

    public boolean isAutoRegister() {
        return autoRegister;
    }

    public void setAutoRegister(boolean autoRegister) {
        this.autoRegister = autoRegister;
    }

    public int getAspectOrder() {
        return aspectOrder;
    }

    public void setAspectOrder(int aspectOrder) {
        this.aspectOrder = aspectOrder;
    }
}
