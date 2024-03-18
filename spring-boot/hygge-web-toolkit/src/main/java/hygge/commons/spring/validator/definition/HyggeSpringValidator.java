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

package hygge.commons.spring.validator.definition;

import hygge.commons.template.definition.HyggeValidator;
import org.springframework.core.Ordered;

/**
 * spring 环境下的 Hygge 校验器，会被统一获取并执行。
 *
 * @author Xavier
 * @date 2023/3/28
 */
public interface HyggeSpringValidator extends HyggeValidator, Ordered {

    /**
     * 不用于与其他 Spring bean 先后顺序调整，仅用于调整 HyggeSpringValidator 实例间的相对排序
     */
    @Override
    default int getOrder() {
        return Ordered.LOWEST_PRECEDENCE - 1000;
    }
}
