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

package hygge.web.util.log.impl;

import hygge.web.util.log.annotation.ControllerLog;
import hygge.web.util.log.base.BaseControllerLogHandler;
import hygge.web.util.log.definition.ControllerLogHandlerFactory;
import hygge.web.util.log.enums.ControllerLogType;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Method;

/**
 * 默认的 ControllerLogHandler 工厂实现
 *
 * @author Xavier
 * @date 2022/7/15
 * @since 1.0
 */
public class DefaultControllerLogHandlerFactory implements ControllerLogHandlerFactory, BeanPostProcessor {
    @Override
    public BaseControllerLogHandler createHandler(Method method, ControllerLogType type, String path, ControllerLog configuration) {
        return new DefaultControllerLogHandler(method, type, path, configuration);
    }
}
