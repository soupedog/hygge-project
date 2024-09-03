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

package hygge.util.generator.java.bo;

import hygge.commons.constant.enums.DateTimeFormatModeEnum;
import hygge.util.UtilCreator;
import hygge.util.definition.TimeHelper;

/**
 * Java 类生成器配置项
 *
 * @author Xavier
 * @date 2024/3/25
 * @since 1.0
 */
public class JavaGeneratorConfiguration {
    protected boolean lombokEnable = false;
    protected String author = "Hygge Generator";
    protected String date = UtilCreator.INSTANCE.getDefaultInstance(TimeHelper.class).format(System.currentTimeMillis(), DateTimeFormatModeEnum.DATE);
    protected boolean enumPropertyModifiable = false;

    public boolean isLombokEnable() {
        return lombokEnable;
    }

    public void setLombokEnable(boolean lombokEnable) {
        this.lombokEnable = lombokEnable;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isEnumPropertyModifiable() {
        return enumPropertyModifiable;
    }

    public void setEnumPropertyModifiable(boolean enumPropertyModifiable) {
        this.enumPropertyModifiable = enumPropertyModifiable;
    }
}
