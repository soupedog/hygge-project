package hygge.web.utils.http.definitions;

import hygge.web.utils.http.configuration.HttpHelperConfig;
import org.springframework.web.client.RestTemplate;

/**
 * 网络请求工具 RestTemplate 构造器
 *
 * @author Xavier
 * @date 2022/7/12
 * @since 1.0
 */
public interface HttpHelperRestTemplateFactory {
    /**
     * 根据配置项获取对应 RestTemplate 对象
     */
    RestTemplate getInstance(HttpHelperConfig config);
}
