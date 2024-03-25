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

package hygge.web.util.http.definition;

import hygge.web.util.http.configuration.HttpHelperRequestConfiguration;
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
    RestTemplate getInstance(HttpHelperRequestConfiguration config);

    /**
     * 获取请求默认配置项
     */
    HttpHelperRequestConfiguration getDefaultConfiguration();
}
