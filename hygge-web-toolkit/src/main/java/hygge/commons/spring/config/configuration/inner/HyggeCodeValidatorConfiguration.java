package hygge.commons.spring.config.configuration.inner;

import hygge.commons.constant.enums.GlobalHyggeCodeEnum;
import hygge.commons.spring.config.configuration.definition.HyggeSpringConfigurationProperties;

/**
 * HyggeCode 校验配置项
 *
 * @author Xavier
 * @date 2023/3/28
 * @since 1.0
 */
public class HyggeCodeValidatorConfiguration implements HyggeSpringConfigurationProperties {
    /**
     * 自定义 HyggeCode 实现类所在包路径。 ({@link GlobalHyggeCodeEnum} 会自动追加，无需手工指定)
     */
    private String basePackages;
    private Boolean uniqueEnable;

    public String getBasePackages() {
        return basePackages;
    }

    public void setBasePackages(String basePackages) {
        this.basePackages = basePackages;
    }

    public Boolean getUniqueEnable() {
        return uniqueEnable;
    }

    public void setUniqueEnable(Boolean uniqueEnable) {
        this.uniqueEnable = uniqueEnable;
    }
}
