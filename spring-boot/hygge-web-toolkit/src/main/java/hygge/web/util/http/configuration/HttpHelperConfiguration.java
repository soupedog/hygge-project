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
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.convert.DurationUnit;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

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

    /**
     * 网络请求工具 日志格式 类型
     *
     * @author Xavier
     * @date 2022/7/12
     * @since 1.0
     */
    public enum HttpLogType {
        /**
         * 不输出日志
         */
        NONE,
        /**
         * 标准
         */
        STANDARD,
        /**
         * 标准的基础上去除 response 的 Headers
         */
        NO_RESPONSE_HEADERS,
        /**
         * 标准的基础上去除 request、response 的 Headers
         */
        NO_HEADERS,
        /**
         * 标准的基础上去除 request、response 的 body
         */
        NO_BODY,
        /**
         * 标准的基础上去除 request、response 的 Headers 与 body
         */
        NO_HEADERS_BODY,
        ;
    }

    /**
     * 网络请求工具配置项 内嵌配置项
     *
     * @author Xavier
     * @date 2022/7/12
     * @since 1.0
     */
    public static class ConnectionConfiguration {
        @DurationUnit(ChronoUnit.SECONDS)
        private Duration connectTimeOut = Duration.ofSeconds(30);
        @DurationUnit(ChronoUnit.SECONDS)
        private Duration readTimeOut = Duration.ofSeconds(30);
        /**
         * 全体域合计最大并发
         */
        private int maxTotal = 400;
        /**
         * 每个域下最大并发
         */
        private int maxPerRoute = 200;

        public Duration getConnectTimeOut() {
            return connectTimeOut;
        }

        public void setConnectTimeOut(Duration connectTimeOut) {
            this.connectTimeOut = connectTimeOut;
        }

        public Duration getReadTimeOut() {
            return readTimeOut;
        }

        public void setReadTimeOut(Duration readTimeOut) {
            this.readTimeOut = readTimeOut;
        }

        public int getMaxTotal() {
            return maxTotal;
        }

        public void setMaxTotal(int maxTotal) {
            this.maxTotal = maxTotal;
        }

        public int getMaxPerRoute() {
            return maxPerRoute;
        }

        public void setMaxPerRoute(int maxPerRoute) {
            this.maxPerRoute = maxPerRoute;
        }
    }
}
