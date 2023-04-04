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

package hygge.web.util.http.configuration;

import hygge.commons.spring.config.configuration.definition.HyggeSpringConfigurationProperties;
import hygge.web.util.http.configuration.inner.ConnectionConfiguration;
import hygge.web.util.http.configuration.enums.HttpLogType;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 网络请求工具配置项
 *
 * @author Xavier
 * @date 2022/7/12
 * @since 1.0
 */
@ConfigurationProperties(prefix = "hygge.util.http-helper.default")
public class HttpHelperConfiguration implements HyggeSpringConfigurationProperties {
    private boolean autoRegister = true;
    private boolean ignoreSSL = false;
    private HttpLogType logType = HttpLogType.NO_RESPONSE_HEADERS;
    private ConnectionConfiguration connection = new ConnectionConfiguration();

    public boolean isAutoRegister() {
        return autoRegister;
    }

    public void setAutoRegister(boolean autoRegister) {
        this.autoRegister = autoRegister;
    }

    public boolean isIgnoreSSL() {
        return ignoreSSL;
    }

    public void setIgnoreSSL(boolean ignoreSSL) {
        this.ignoreSSL = ignoreSSL;
    }

    public HttpLogType getLogType() {
        return logType;
    }

    public void setLogType(HttpLogType logType) {
        this.logType = logType;
    }

    public ConnectionConfiguration getConnection() {
        return connection;
    }

    public void setConnection(ConnectionConfiguration connection) {
        this.connection = connection;
    }
}
