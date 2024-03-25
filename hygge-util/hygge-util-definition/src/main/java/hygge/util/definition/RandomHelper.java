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

package hygge.util.definition;

import hygge.commons.constant.enums.StringCategoryEnum;

/**
 * 随机生成工具类
 *
 * @author Xavier
 * @date 2022/6/25
 * @since 1.0
 */
public interface RandomHelper extends HyggeUtil {
    /**
     * 生成随机数字(闭区间)
     *
     * @param minValue 随机值最小值
     * @param maxValue 随机值最大值
     * @return 一个随机整数
     */
    int getRandomInteger(int minValue, int maxValue);

    /**
     * 可重复地生成随机字符串
     *
     * @param size           随机字符串目标长度
     * @param stringCategory 随机字符池类型
     * @return 随机字符串
     */
    String getRandomString(int size, StringCategoryEnum... stringCategory);

    /**
     * 生成 UUID
     *
     * @param withOutSpecialCharacters 是否包含特殊字符 "-"
     * @return UUID
     */
    String getUniversallyUniqueIdentifier(boolean withOutSpecialCharacters);
}
