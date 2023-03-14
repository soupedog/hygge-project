package hygge.web.util.http.definition;


import hygge.web.util.http.bo.HttpResponse;
import hygge.web.util.http.configuration.HttpHelperRequestConfiguration;

/**
 * 网络请求工具日志记录器
 *
 * @author Xavier
 * @date 2022/7/12
 * @since 1.0
 */
public interface HttpHelperLogger {
    /**
     * 进行日志的输出
     *
     * @param config 当前请求的配置项
     * @param result 当前请的响应结果
     */
    void logOutput(HttpHelperRequestConfiguration config, HttpResponse<?, ?> result);
}
