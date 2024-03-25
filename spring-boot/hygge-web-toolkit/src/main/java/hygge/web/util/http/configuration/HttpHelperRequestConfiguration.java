/*
 * Copyright 2022-2024 the original author or authors.
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

import java.util.StringJoiner;

import static hygge.web.util.http.configuration.HttpHelperConfiguration.HttpLogType;

/**
 * HttpHelper 发起网络请求配置项
 *
 * @author Xavier
 * @date 2022/7/12
 * @since 1.0
 */
public class HttpHelperRequestConfiguration {
    protected final HttpLogType logType;
    protected final boolean ignoreSSL;
    protected final long connectTimeOutMilliseconds;
    protected final long readTimeOutMilliseconds;

    public HttpHelperRequestConfiguration(HttpLogType logType, boolean ignoreSSL, long connectTimeOutMilliseconds, long readTimeOutMilliseconds) {
        this.logType = logType;
        this.ignoreSSL = ignoreSSL;
        this.connectTimeOutMilliseconds = connectTimeOutMilliseconds;
        this.readTimeOutMilliseconds = readTimeOutMilliseconds;
    }

    public HttpLogType logType() {
        return logType;
    }

    public boolean ignoreSSL() {
        return ignoreSSL;
    }

    public long connectTimeOutMilliseconds() {
        return connectTimeOutMilliseconds;
    }

    public long readTimeOutMilliseconds() {
        return readTimeOutMilliseconds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HttpHelperRequestConfiguration that = (HttpHelperRequestConfiguration) o;

        if (ignoreSSL != that.ignoreSSL) return false;
        if (connectTimeOutMilliseconds != that.connectTimeOutMilliseconds) return false;
        if (readTimeOutMilliseconds != that.readTimeOutMilliseconds) return false;
        return logType == that.logType;
    }

    @Override
    public int hashCode() {
        int result = logType != null ? logType.hashCode() : 0;
        result = 31 * result + (ignoreSSL ? 1 : 0);
        result = 31 * result + (int) (connectTimeOutMilliseconds ^ (connectTimeOutMilliseconds >>> 32));
        result = 31 * result + (int) (readTimeOutMilliseconds ^ (readTimeOutMilliseconds >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return new StringJoiner(",", "{", "}")
                .add("\"logType\":\"" + logType + "\"")
                .add("\"ignoreSSL\":" + ignoreSSL)
                .add("\"connectTimeOutMilliseconds\":" + connectTimeOutMilliseconds)
                .add("\"readTimeOutMilliseconds\":" + readTimeOutMilliseconds)
                .toString();
    }
}
