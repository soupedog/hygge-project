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

package hygge.commons.constant;

import java.io.File;

/**
 * 一些业务开发需要的常量
 *
 * @author Xavier
 * @date 2023/3/13
 * @since 1.0
 */
public class ConstantParameters {
    private ConstantParameters() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * 与宿主系统相适配的换行符
     */
    public static final String LINE_SEPARATOR = System.lineSeparator();
    /**
     * 与宿主系统相适配的目录分隔符
     */
    public static final String FILE_SEPARATOR = File.separator;
    /**
     * 四个空格符
     */
    public static final String BLANK_4 = "    ";
}
