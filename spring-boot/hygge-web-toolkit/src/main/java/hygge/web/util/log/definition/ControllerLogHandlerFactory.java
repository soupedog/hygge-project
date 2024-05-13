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

package hygge.web.util.log.definition;

import hygge.commons.annotation.HyggeExpressionForInputFunction;
import hygge.commons.annotation.HyggeExpressionForOutputFunction;
import hygge.web.util.log.base.BaseControllerLogHandler;
import hygge.web.util.log.enums.ControllerLogType;

import java.util.Collection;

/**
 * ControllerLogHandler 工厂
 *
 * @author Xavier
 * @date 2022/7/15
 * @since 1.0
 */
public interface ControllerLogHandlerFactory {
    /**
     * 创建一个 BaseControllerLogHandler 实例
     *
     * @param type                     拦截请求的类型
     * @param path                     拦截请求的 path
     * @param inputParamNames          入参名称列表
     * @param ignoreParamNames         需忽略的入参名称列表
     * @param inputParamGetExpressions 用于记录日志的入参获取表达式集合
     * @param outputParamExpressions   用于记录日志的出参获取表达式集合
     */
    BaseControllerLogHandler createHandler(ControllerLogType type, String path, String[] inputParamNames, Collection<String> ignoreParamNames, Collection<HyggeExpressionForInputFunction> inputParamGetExpressions, Collection<HyggeExpressionForOutputFunction> outputParamExpressions);
}
