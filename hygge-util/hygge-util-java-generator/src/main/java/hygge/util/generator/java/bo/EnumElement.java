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

import hygge.commons.constant.enums.ColumnTypeEnum;
import hygge.commons.exception.LightRuntimeException;

import java.util.List;

import static hygge.commons.constant.enums.ColumnTypeEnum.analyseColumnType;

public class EnumElement {
    /**
     * 枚举值名称
     */
    private String name;

    /**
     * 枚举值描述(将作为 java doc)
     */
    private String description;

    /**
     * 枚举构造参数
     */
    private List<Object> params;

    public String formatParam(Object param) {
        ColumnTypeEnum type = analyseColumnType(param);
        switch (type) {
            case NULL:
                return "null";
            case STRING:
                return "\"" + param.toString() + "\"";
            case INTEGER:
            case BOOLEAN:
                return param.toString();
            case SHORT:
                return "(short) " + param.toString();
            case BYTE:
                return "(byte) " + param.toString();
            case LONG:
                return param.toString() + " l";
            case FLOAT:
                return param.toString() + " f";
            case DOUBLE:
                return param.toString() + " d";
            default:
                throw new LightRuntimeException("Unsupported EnumValue:" + param + " .");
        }
    }

    public static EnumElementBuilder builder() {
        return new EnumElementBuilder();
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public List<Object> getParams() {
        return this.params;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setParams(List<Object> params) {
        this.params = params;
    }

    public EnumElement() {
    }

    public EnumElement(String name, String description, List<Object> params) {
        this.name = name;
        this.description = description;
        this.params = params;
    }

    public static class EnumElementBuilder {
        private String name;
        private String description;
        private List<Object> params;

        EnumElementBuilder() {
        }

        public EnumElementBuilder name(String name) {
            this.name = name;
            return this;
        }

        public EnumElementBuilder description(String description) {
            this.description = description;
            return this;
        }

        public EnumElementBuilder params(List<Object> params) {
            this.params = params;
            return this;
        }

        public EnumElement build() {
            return new EnumElement(this.name, this.description, this.params);
        }

        public String toString() {
            return "EnumElement.EnumElementBuilder(name=" + this.name + ", description=" + this.description + ", params=" + this.params + ")";
        }
    }
}
