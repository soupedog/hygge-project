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
