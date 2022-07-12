package hygge.web.utils.http.configuration.inner;

import org.springframework.boot.convert.DurationUnit;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

/**
 * 网络请求工具配置项 内嵌配置项
 *
 * @author Xavier
 * @date 2022/7/12
 * @since 1.0
 */
public class ConnectionConfiguration {
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
