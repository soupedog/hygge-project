package hygge.web.util.http.impl;

import hygge.commons.template.container.base.AbstractHyggeKeeper;
import hygge.web.util.http.configuration.HttpHelperRequestConfiguration;
import org.springframework.web.client.RestTemplate;

/**
 * RestTemplate 缓存容器
 *
 * @author Xavier
 * @date 2023/8/28
 * @since 1.0
 */
public class RestTemplateKeeper extends AbstractHyggeKeeper<HttpHelperRequestConfiguration, RestTemplate> {
}
