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

package hygge.web.util.log.inner;

import hygge.commons.annotation.HyggeExpressionForInputFunction;
import hygge.commons.template.container.base.AbstractHyggeKeeper;
import org.springframework.expression.Expression;

import java.util.Map;

/**
 * Expression 缓存容器</br>
 * key:{@link HyggeExpressionForInputFunction#name()}
 *
 * @author Xavier
 * @date 2022/7/14
 * @since 1.0
 */
public class ExpressionCache extends AbstractHyggeKeeper<String, Expression> {
    public Map<String, Expression> getContainer() {
        return container;
    }
}
