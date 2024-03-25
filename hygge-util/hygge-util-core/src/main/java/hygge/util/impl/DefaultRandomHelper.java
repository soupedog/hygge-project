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

package hygge.util.impl;


import hygge.util.base.BaseRandomHelper;

/**
 * 默认的 RandomHelper 实现类
 *
 * @author Xavier
 * @date 2022/7/10
 * @since 1.0
 */
public class DefaultRandomHelper extends BaseRandomHelper {
    @Override
    protected char hookSingleChar(char singleChar) {
        switch (singleChar) {
            // 数字0被替换成2
            case '0':
                singleChar = '2';
                break;
            // 大写字母 O 被替换成 X
            case 'O':
                singleChar = 'X';
                break;
            // 小写字母 o 被替换成 x
            case 'o':
                singleChar = 'x';
                break;
            default:
                break;
        }
        return singleChar;
    }
}
