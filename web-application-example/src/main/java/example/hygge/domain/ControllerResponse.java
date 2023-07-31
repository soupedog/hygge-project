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

package example.hygge.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import hygge.web.template.HyggeControllerResponse;

/**
 * 自定义 Controller 返回类型
 *
 * @author Xavier
 * @date 2022/7/7
 * @since 1.0
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ControllerResponse<T> extends HyggeControllerResponse<Integer, T> {
}
