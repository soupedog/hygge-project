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

package hygge.web.util.log;

import hygge.commons.annotation.HyggeExpressionInfo;
import hygge.web.template.definition.AutoLogController;
import hygge.web.util.log.annotation.ControllerLog;
import hygge.web.util.log.base.BaseControllerLogHandler;
import hygge.web.util.log.definition.ControllerLogHandlerFactory;
import hygge.web.util.log.enums.ControllerLogType;
import hygge.web.util.log.inner.ControllerLogHandlerCache;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.StandardReflectionParameterNameDiscoverer;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Controller 层自动日志的 PointCut，进行初步的分析筛选并保存必要的信息
 *
 * @author Xavier
 * @date 2022/7/15
 * @since 1.0
 */
public class ControllerLogPointCut extends StaticMethodMatcherPointcut implements BeanPostProcessor {
    /**
     * Spring 6.x 未来会移除 {@link LocalVariableTableParameterNameDiscoverer}，我们未来计划使用 {@link StandardReflectionParameterNameDiscoverer} 进行替换，尽管它存在前置要求<br/>
     * 未替换前，将无法与 Spring boot 3.2.x 保持兼容
     */
    protected static final ParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
    protected ControllerLogHandlerCache controllerLogHandlerCache;
    protected ControllerLogHandlerFactory controllerLogHandlerFactory;

    public ControllerLogPointCut(ControllerLogHandlerCache controllerLogHandlerCache, ControllerLogHandlerFactory controllerLogHandlerFactory) {
        this.controllerLogHandlerCache = controllerLogHandlerCache;
        this.controllerLogHandlerFactory = controllerLogHandlerFactory;
    }

    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        if (!AutoLogController.class.isAssignableFrom(targetClass)) {
            // 如果不是 HyggeController 的子类，直接跳过
            return false;
        }

        ControllerLog configuration = AnnotationUtils.getAnnotation(method, ControllerLog.class);
        if (configuration != null && !configuration.enable()) {
            // 如果使用配置项注解主动关闭当前方法的自动日志，直接跳过
            return false;
        }

        if (controllerLogHandlerCache.getValue(method) != null) {
            // 已初始化完毕的方法无需再次初始化 BaseControllerLogHandler 对象
            return true;
        }

        ControllerLogType type;
        String path;
        String[] inputParamNames = parameterNameDiscoverer.getParameterNames(method);
        Collection<String> ignoreParamNames;
        Collection<HyggeExpressionInfo> inputParamGetExpressions;
        Collection<HyggeExpressionInfo> outputParamExpressions;

        if (method.isAnnotationPresent(GetMapping.class)) {
            GetMapping getMapping = AnnotationUtils.getAnnotation(method, GetMapping.class);
            type = ControllerLogType.GET;
            path = getPathInfo(Objects.requireNonNull(getMapping).path(), getMapping.value());
        } else if (method.isAnnotationPresent(PostMapping.class)) {
            PostMapping postMapping = AnnotationUtils.getAnnotation(method, PostMapping.class);
            type = ControllerLogType.POST;
            path = getPathInfo(Objects.requireNonNull(postMapping).path(), postMapping.value());
        } else if (method.isAnnotationPresent(PatchMapping.class)) {
            PatchMapping patchMapping = AnnotationUtils.getAnnotation(method, PatchMapping.class);
            type = ControllerLogType.PATCH;
            path = getPathInfo(Objects.requireNonNull(patchMapping).path(), patchMapping.value());
        } else if (method.isAnnotationPresent(PutMapping.class)) {
            PutMapping putMapping = AnnotationUtils.getAnnotation(method, PutMapping.class);
            type = ControllerLogType.PUT;
            path = getPathInfo(Objects.requireNonNull(putMapping).path(), putMapping.value());
        } else if (method.isAnnotationPresent(DeleteMapping.class)) {
            DeleteMapping deleteMapping = AnnotationUtils.getAnnotation(method, DeleteMapping.class);
            type = ControllerLogType.DELETE;
            path = getPathInfo(Objects.requireNonNull(deleteMapping).path(), deleteMapping.value());
        } else {
            type = ControllerLogType.NONE;
            path = null;
        }

        if (ControllerLogType.NONE.equals(type)) {
            // 如果当前方法没有标记 GetMapping、PostMapping、PatchMapping、PutMapping、DeleteMapping，直接跳过
            return false;
        }

        if (configuration != null) {
            ignoreParamNames = new ArrayList<>(Arrays.asList(configuration.ignoreParamNames()));
            inputParamGetExpressions = new ArrayList<>(Arrays.asList(configuration.inputParamGetExpressions()));
            outputParamExpressions = new ArrayList<>(Arrays.asList(configuration.outputParamExpressions()));
        } else {
            ignoreParamNames = new ArrayList<>(0);
            inputParamGetExpressions = new ArrayList<>(0);
            outputParamExpressions = new ArrayList<>(0);
        }

        BaseControllerLogHandler handler = controllerLogHandlerFactory.createHandler(type, path, inputParamNames, ignoreParamNames, inputParamGetExpressions, outputParamExpressions);
        controllerLogHandlerCache.saveValue(method, handler);
        return true;
    }

    protected String getPathInfo(String[] paths, String[] values) {
        Set<String> result = new HashSet<>();
        if (paths.length > 0) {
            result.addAll(Arrays.asList(paths));
        }
        if (values.length > 0) {
            result.addAll(Arrays.asList(values));
        }

        if (!result.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            for (String item : result) {
                builder.append(item);
                builder.append(",");
            }
            builder.delete(builder.length() - 1, builder.length());
            return builder.toString();
        } else {
            return null;
        }
    }
}
