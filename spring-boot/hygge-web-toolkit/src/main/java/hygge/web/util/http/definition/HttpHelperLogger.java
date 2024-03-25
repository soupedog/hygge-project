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
