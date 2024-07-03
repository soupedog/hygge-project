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

import hygge.web.template.definition.AutoLogController;
import hygge.web.util.log.annotation.ControllerLog;
import hygge.web.util.log.base.BaseControllerLogHandler;
import hygge.web.util.log.definition.ControllerLogHandlerFactory;
import hygge.web.util.log.enums.ControllerLogType;
import hygge.web.util.log.inner.ControllerLogHandlerCache;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.lang.reflect.Method;
import java.util.Arrays;
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

        if (controllerLogHandlerCache.getValue(method) != null) {
            // 已初始化完毕的方法无需再次初始化 BaseControllerLogHandler 对象
            return true;
        }

        ControllerLogType type;
        String path;

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
            type = ControllerLogType.UNKNOWN;
            path = null;
        }

        // 不支持的方法无需作为切入点
        if(ControllerLogType.UNKNOWN.equals(type)){
            return false;
        }

        BaseControllerLogHandler handler = controllerLogHandlerFactory.createHandler(method, type, path, configuration);
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
