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

import hygge.web.util.log.ControllerLogContext;
import hygge.web.util.log.annotation.ControllerLog;
import hygge.web.util.log.base.BaseControllerLogHandler;
import hygge.web.util.log.bo.ControllerLogInfo;
import hygge.web.util.log.enums.ControllerLogType;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Method;

/**
 * BaseControllerLogHandler 的默认实现
 *
 * @author Xavier
 * @date 2024/7/3
 * @since 1.0
 */
public class DefaultControllerLogHandler extends BaseControllerLogHandler {
    protected DefaultControllerLogHandler(Method method, ControllerLogType type, String path, ControllerLog configuration) {
        super(method, type, path, configuration);
    }

    @Override
    protected void hook(ControllerLogContext context, MethodInvocation methodInvocation) {
        // 默认什么也不做
    }

    @Override
    protected Object defaultProcess(ControllerLogContext context, MethodInvocation methodInvocation) throws Throwable {
        ControllerLogInfo controllerLogInfo = context.getLogInfo();
        controllerLogInfo.setType(type);
        controllerLogInfo.setPath(path);

        if (inputParamEnable) {
            Object[] inputParameterValues = methodInvocation.getArguments();
            controllerLogInfo.setInput(getInputParam(inputParameterValues));
        }

        Object responseEntity = methodInvocation.proceed();
        context.saveObject(ControllerLogContext.Key.RAW_RESPONSE, responseEntity);

        if (outputParamEnable) {
            Object responseEntityForLog;

            if (responseEntity instanceof ResponseEntity) {
                responseEntityForLog = ((ResponseEntity<?>) responseEntity).getBody();
            } else {
                responseEntityForLog = responseEntity;
            }

            controllerLogInfo.setOutput(getOutputParam(responseEntityForLog));
        }

        return responseEntity;
    }

    @Override
    protected Object processForNoneType(ControllerLogContext context, MethodInvocation methodInvocation) throws Throwable {
        return methodInvocation.proceed();
    }
}
