package hygge.commons.spring.listener;

import hygge.commons.spring.HyggeSpringContext;
import hygge.commons.spring.enums.DeploymentEnvironmentEnum;
import hygge.utils.UtilsCreator;
import hygge.utils.definitions.ParameterHelper;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * 初始化 HyggeContext 的监听器
 *
 * @author Xavier
 * @date 2022/6/28
 * @since 1.0
 */
public class HyggeSpringContextInitListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {
    private static final ParameterHelper parameterHelper = UtilsCreator.INSTANCE.getDefaultInstance(ParameterHelper.class);

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent applicationEnvironmentPreparedEvent) {
        ConfigurableEnvironment configurableEnvironment = applicationEnvironmentPreparedEvent.getEnvironment();
        String applicationName = configurableEnvironment.getProperty("spring.application.name");

        HyggeSpringContext.setAppName(parameterHelper.stringNotEmpty("spring.application.name", (Object) applicationName));
        HyggeSpringContext.setEnvironmentModeEnum(analyzeEnvironment(configurableEnvironment.getActiveProfiles()));
        HyggeSpringContext.setConfigurableEnvironment(configurableEnvironment);
    }

    private DeploymentEnvironmentEnum analyzeEnvironment(String[] profiles) {
        if (profiles == null || profiles.length < 1) {
            return DeploymentEnvironmentEnum.DEV;
        } else {
            if (arrayContains(DeploymentEnvironmentEnum.PROD.name(), profiles)) {
                return DeploymentEnvironmentEnum.PROD;
            } else if (arrayContains(DeploymentEnvironmentEnum.SIM.name(), profiles)) {
                return DeploymentEnvironmentEnum.SIM;
            } else if (arrayContains(DeploymentEnvironmentEnum.UAT.name(), profiles)) {
                return DeploymentEnvironmentEnum.UAT;
            } else if (arrayContains(DeploymentEnvironmentEnum.SIT.name(), profiles)) {
                return DeploymentEnvironmentEnum.SIT;
            } else {
                return DeploymentEnvironmentEnum.DEV;
            }
        }
    }

    private boolean arrayContains(String target, String[] array) {
        String targetTemp = target.toUpperCase();
        for (String itemTemp : array) {
            if (targetTemp.equals(itemTemp.toUpperCase())) {
                return true;
            }
        }
        return false;
    }
}
