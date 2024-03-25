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

package hygge.web.util.log;

import hygge.web.util.log.inner.ControllerLogHandlerCache;
import hygge.web.util.log.base.BaseControllerLogHandler;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * Controller 自动日志拦截器
 *
 * @author Xavier
 * @date 2022/7/15
 * @since 1.0
 */
public class ControllerLogInterceptor implements MethodInterceptor, BeanPostProcessor {
    protected ControllerLogHandlerCache controllerLogHandlerCache;

    public ControllerLogInterceptor(ControllerLogHandlerCache controllerLogHandlerCache) {
        this.controllerLogHandlerCache = controllerLogHandlerCache;
    }

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        BaseControllerLogHandler baseControllerLogHandler = controllerLogHandlerCache.getValue(methodInvocation.getMethod());
        return baseControllerLogHandler.executeHandler(methodInvocation);
    }
}
