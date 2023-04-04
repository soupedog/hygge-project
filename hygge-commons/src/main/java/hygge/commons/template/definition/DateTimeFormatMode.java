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

import java.time.format.DateTimeFormatter;

/**
 * 日期格式化模式
 *
 * @author Xavier
 * @date 2022/6/25
 * @since 1.0
 */
public interface DateTimeFormatMode {
    /**
     * 获取字符串时间戳模板
     */
    String getPattern();

    /**
     * 获取字符串时间戳模板对应的 DateTimeFormatter
     */
    DateTimeFormatter getDateTimeFormatter();

    /**
     * 当前字符串时间戳模板是否已经包含了时区信息
     */
    boolean withTimeZone();

}
