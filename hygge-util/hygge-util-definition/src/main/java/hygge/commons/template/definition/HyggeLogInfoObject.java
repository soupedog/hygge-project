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

package hygge.commons.template.definition;

/**
 * 标记当前对象为日志对象，提供了生成自定义日志(用于日志信息的脱敏、剔除冗余信息等操作)的专有方法
 *
 * @author Xavier
 * @date 2022/7/12
 * @since 1.0
 */
public interface HyggeLogInfoObject {
    /**
     * 将当前对象转化为自定义内容的 json 格式日志信息
     */
    String toJsonLogInfo();
}
