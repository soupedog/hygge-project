package hygge.commons.spring.config.configuration;

import hygge.commons.spring.config.configuration.definition.HyggeSpringConfigurationProperties;
import hygge.commons.spring.config.configuration.inner.HyggeCodeValidatorConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * HyggeSpringValidator 配置项
 *
 * @author Xavier
 * @date 2023/3/28
 * @since 1.0
 */
@ConfigurationProperties(prefix = "hygge.web-toolkit.validator")
public class HyggeSpringValidatorConfiguration implements HyggeSpringConfigurationProperties {
    private HyggeCodeValidatorConfiguration hyggeCode;

    public HyggeCodeValidatorConfiguration getHyggeCode() {
        return hyggeCode;
    }

    public void setHyggeCode(HyggeCodeValidatorConfiguration hyggeCode) {
        this.hyggeCode = hyggeCode;
    }
}
