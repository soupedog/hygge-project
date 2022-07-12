package hygge.web.utils.http.configuration;

import java.util.StringJoiner;

/**
 * 网络请求工具配置项
 *
 * @author Xavier
 * @date 2022/7/12
 * @since 1.0
 */
public class HttpHelperConfig {
    protected final HttpLogType logType;
    protected final boolean ignoreSSL;
    protected final long connectTimeOutMilliseconds;
    protected final long readTimeOutMilliseconds;

    public HttpHelperConfig(HttpLogType logType, boolean ignoreSSL, long connectTimeOutMilliseconds, long readTimeOutMilliseconds) {
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

        HttpHelperConfig that = (HttpHelperConfig) o;

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
