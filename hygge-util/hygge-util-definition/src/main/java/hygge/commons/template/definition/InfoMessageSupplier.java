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

package hygge.commons.template.definition;

/**
 * 信息提供者，内部包含一些标准新消息生成方法
 *
 * @author Xavier
 * @date 2022/6/27
 * @since 1.0
 */
public interface InfoMessageSupplier {
    default String unexpectedStringLength(String target, String targetName, long minLength, long maxLength) {
        String finalValueInfo = null;
        if (target != null) {
            finalValueInfo = target.length() + "(" + target + ")";
        }
        return "Unexpected " + targetName + ", its length must be between " + minLength + " and " + maxLength + ", but was " + finalValueInfo + ".";
    }

    default String unexpectedNumberValue(Number target, String targetName, String instanceName, Number minValue, Number maxValue) {
        return "Unexpected " + targetName + ", it must be an instance of " + instanceName + ", and its value must be between " + minValue + " and " + maxValue + ", but was " + target + "(" + target.getClass().getSimpleName() + ").";
    }

    default String unexpectedClass(String targetName, Class<?> targetClass, Class<?> expect) {
        String finalClassInfo = null;
        if (targetClass != null) {
            finalClassInfo = targetClass.getName();
        }
        return "Unexpected " + targetName + ", it must be an instance of " + expect.getName() + ", but was " + finalClassInfo + ".";
    }

    default String unexpectedObject(String parameterName, Object target, String targetName, Object expect) {
        return "Unexpected " + targetName + ", its " + parameterName + " expected " + expect + " but was " + target + ".";
    }

    default String unexpectedObjectRange(String parameterName, Object target, String targetName, Object minValue, Object maxValue) {
        return "Unexpected " + targetName + ", its " + parameterName + " must be between " + minValue + " and " + maxValue + ", but was " + target + ".";
    }
}
