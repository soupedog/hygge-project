package hygge.web.utils.log.configuration;

import hygge.commons.spring.config.configuration.HyggeSpringProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Controller 层自动日志配置项
 *
 * @author Xavier
 * @date 2022/7/12
 * @since 1.0
 */
@ConfigurationProperties(prefix = "hygge.web-toolkit.controller.log")
public class ControllerLogConfiguration implements HyggeSpringProperties {
    private boolean autoRegister = true;

    public boolean isAutoRegister() {
        return autoRegister;
    }

    public void setAutoRegister(boolean autoRegister) {
        this.autoRegister = autoRegister;
    }
}
