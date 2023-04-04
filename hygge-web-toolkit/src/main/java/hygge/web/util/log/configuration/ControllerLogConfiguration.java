/*
 * Copyright 2022-2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
