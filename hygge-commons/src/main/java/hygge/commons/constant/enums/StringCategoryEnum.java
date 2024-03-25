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

package hygge.commons.constant.enums;

/**
 * 字符串取值范围类型枚举
 *
 * @author Xavier
 * @date 2022/6/25
 * @since 1.0
 */
@SuppressWarnings("java:S115")
public enum StringCategoryEnum {
    /**
     * 所有数字类型<br/>
     * 0~9
     */
    NUMBER(48, 10),
    /**
     * 所有大写字母<br/>
     * 大写字母A(65)~Z(90)
     */
    A_Z(65, 26),
    /**
     * 所有小写字母<br/>
     * 小写字母A(97)~Z(122)
     */
    a_z(97, 26),
    ;

    /**
     * 首字符 ASCII 码起始序号
     */
    private final Integer asciiStartPoint;
    /**
     * 该类型字符集字符总数
     */
    private final Integer totalSize;

    StringCategoryEnum(Integer asciiStartPoint, Integer totalSize) {
        this.asciiStartPoint = asciiStartPoint;
        this.totalSize = totalSize;
    }

    public Integer getAsciiStartPoint() {
        return asciiStartPoint;
    }

    public Integer getTotalSize() {
        return totalSize;
    }
}
