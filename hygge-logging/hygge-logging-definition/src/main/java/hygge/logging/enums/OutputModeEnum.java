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

package hygge.logging.enums;

/**
 * 日志输出的模式
 *
 * @author Xavier
 * @date 2022/6/28
 * @since 1.0
 */
public enum OutputModeEnum {
    /**
     * 控制台模式
     */
    CONSOLE("CONSOLE"),
    /**
     * 文件模式
     */
    FILE("FILE"),
    /**
     * 控制台、文件均进行日志输出
     */
    CONSOLE_AND_FILE("CONSOLE_AND_FILE"),
    ;

    OutputModeEnum(String description) {
        this.description = description;
    }

    /**
     * 描述
     */
    private final String description;

    public String getDescription() {
        return description;
    }
}
