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


import hygge.util.UtilCreator;
import hygge.util.definition.CollectionHelper;
import hygge.util.definition.ParameterHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * 类信息属性
 *
 * @author Xavier
 * @date 2024/3/25
 * @since 1.0
 */
public class Property {
    protected static final CollectionHelper collectionHelper = UtilCreator.INSTANCE.getDefaultInstance(CollectionHelper.class);
    protected static final ParameterHelper parameterHelper = UtilCreator.INSTANCE.getDefaultInstance(ParameterHelper.class);
    /**
     * 属性名称
     */
    private String name;
    /**
     * 属性类型信息
     */
    private ClassInfo classInfo;
    /**
     * 属性描述(将作为 java doc)
     */
    private String description;
    /**
     * 属性注解信息
     */
    private List<ClassInfo> annotations = new ArrayList<>();
    /**
     * 属性修饰符
     */
    private List<Modifier> modifiers = Modifier.createModifierList(Modifier.PRIVATE);

    private static List<ClassInfo> $default$annotations() {
        return new ArrayList();
    }

    private static List<Modifier> $default$modifiers() {
        return Modifier.createModifierList(new Modifier[]{Modifier.PRIVATE});
    }

    public static PropertyBuilder builder() {
        return new PropertyBuilder();
    }

    public String getName() {
        return this.name;
    }

    public ClassInfo getClassInfo() {
        return this.classInfo;
    }

    public String getDescription() {
        return this.description;
    }

    public List<ClassInfo> getAnnotations() {
        return this.annotations;
    }

    public List<Modifier> getModifiers() {
        return this.modifiers;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setClassInfo(ClassInfo classInfo) {
        this.classInfo = classInfo;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAnnotations(List<ClassInfo> annotations) {
        this.annotations = annotations;
    }

    public void setModifiers(List<Modifier> modifiers) {
        this.modifiers = modifiers;
    }

    public Property() {
        this.annotations = $default$annotations();
        this.modifiers = $default$modifiers();
    }

    public Property(String name, ClassInfo classInfo, String description, List<ClassInfo> annotations, List<Modifier> modifiers) {
        this.name = name;
        this.classInfo = classInfo;
        this.description = description;
        this.annotations = annotations;
        this.modifiers = modifiers;
    }

    public static class PropertyBuilder {
        private String name;
        private ClassInfo classInfo;
        private String description;
        private boolean annotations$set;
        private List<ClassInfo> annotations$value;
        private boolean modifiers$set;
        private List<Modifier> modifiers$value;

        PropertyBuilder() {
        }

        public PropertyBuilder name(String name) {
            this.name = name;
            return this;
        }

        public PropertyBuilder classInfo(ClassInfo classInfo) {
            this.classInfo = classInfo;
            return this;
        }

        public PropertyBuilder description(String description) {
            this.description = description;
            return this;
        }

        public PropertyBuilder annotations(List<ClassInfo> annotations) {
            this.annotations$value = annotations;
            this.annotations$set = true;
            return this;
        }

        public PropertyBuilder modifiers(List<Modifier> modifiers) {
            this.modifiers$value = modifiers;
            this.modifiers$set = true;
            return this;
        }

        public Property build() {
            List<ClassInfo> annotations$value = this.annotations$value;
            if (!this.annotations$set) {
                annotations$value = Property.$default$annotations();
            }

            List<Modifier> modifiers$value = this.modifiers$value;
            if (!this.modifiers$set) {
                modifiers$value = Property.$default$modifiers();
            }

            return new Property(this.name, this.classInfo, this.description, annotations$value, modifiers$value);
        }

        public String toString() {
            return "Property.PropertyBuilder(name=" + this.name + ", classInfo=" + this.classInfo + ", description=" + this.description + ", annotations$value=" + this.annotations$value + ", modifiers$value=" + this.modifiers$value + ")";
        }
    }
}
