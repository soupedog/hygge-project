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

package hygge.util.base;


import hygge.commons.constant.enums.StringCategoryEnum;
import hygge.util.UtilCreator;
import hygge.util.definition.ParameterHelper;
import hygge.util.definition.RandomHelper;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 随机生成工具类基类
 *
 * @author Xavier
 * @date 2022/7/10
 * @since 1.0
 */
public abstract class BaseRandomHelper implements RandomHelper {
    protected ParameterHelper parameterHelper = UtilCreator.INSTANCE.getDefaultInstance(ParameterHelper.class);

    /**
     * 每次生成单个随机字符时触发,返回的 char 会影响最后生成字符
     *
     * @param singleChar 当前生成的单个随机字符
     * @return 处理后的随机字符
     */
    protected abstract char hookSingleChar(char singleChar);

    @Override
    public int getRandomInteger(int minValue, int maxValue) {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return random.nextInt(minValue, maxValue + 1);
    }

    @Override
    public String getRandomString(int size, StringCategoryEnum... stringCategoryEnums) {
        parameterHelper.integerFormatNotEmpty("size", size, 1, Integer.MAX_VALUE);
        StringBuilder result = new StringBuilder();
        int currentStringCategoryIndex;
        StringCategoryEnum currentStringCategory;
        char randomTemp;
        for (int i = 0; i < size; i++) {
            currentStringCategoryIndex = ThreadLocalRandom.current().nextInt(stringCategoryEnums.length);
            currentStringCategory = stringCategoryEnums[currentStringCategoryIndex];
            randomTemp = (char) ThreadLocalRandom.current().nextInt(currentStringCategory.getAsciiStartPoint(), currentStringCategory.getAsciiStartPoint() + currentStringCategory.getTotalSize());
            randomTemp = hookSingleChar(randomTemp);
            result.append(randomTemp);
        }
        return result.toString();
    }

    @Override
    public String getUniversallyUniqueIdentifier(boolean withOutSpecialCharacters) {
        return withOutSpecialCharacters ? UUID.randomUUID().toString().replace("-", "")
                : UUID.randomUUID().toString();
    }
}
