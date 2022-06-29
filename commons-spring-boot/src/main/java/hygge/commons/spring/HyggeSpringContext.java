package hygge.commons.spring;

import hygge.commons.spring.enums.DeploymentEnvironmentEnum;
import hygge.commons.spring.listener.HyggeSpringContextInitListener;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * Hygge Spring 环境下系统上下文
 *
 * @author Xavier
 * @date 2022/6/28
 * @since 1.0
 */
public class HyggeSpringContext {
    /**
     * 当前部署环境
     *
     * @see HyggeSpringContextInitListener
     */
    private static volatile DeploymentEnvironmentEnum deploymentEnvironment;
    /**
     * 应用名称
     *
     * @see HyggeSpringContextInitListener
     */
    private static volatile String appName;
    /**
     * 系统环境变量
     *
     * @see HyggeSpringContextInitListener
     */
    private static ConfigurableEnvironment configurableEnvironment;

    public static DeploymentEnvironmentEnum getDeploymentEnvironment() {
        return deploymentEnvironment;
    }

    public static void setDeploymentEnvironment(DeploymentEnvironmentEnum deploymentEnvironment) {
        HyggeSpringContext.deploymentEnvironment = deploymentEnvironment;
    }

    public static String getAppName() {
        return appName;
    }

    public static void setAppName(String appName) {
        HyggeSpringContext.appName = appName;
    }

    public static ConfigurableEnvironment getConfigurableEnvironment() {
        return configurableEnvironment;
    }

    public static void setConfigurableEnvironment(ConfigurableEnvironment configurableEnvironment) {
        HyggeSpringContext.configurableEnvironment = configurableEnvironment;
    }

    public static String toJsonVale() {
        return String.format("{\"environmentModeEnum\":\"%s\",\"appName\":\"%s\"}",
                deploymentEnvironment,
                appName);
    }

    @Override
    public String toString() {
        return toJsonVale();
    }
}
