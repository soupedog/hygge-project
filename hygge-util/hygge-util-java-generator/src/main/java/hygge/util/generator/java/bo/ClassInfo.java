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

import com.fasterxml.jackson.annotation.JsonIgnore;
import hygge.util.UtilCreator;
import hygge.util.definition.CollectionHelper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import static hygge.util.constant.ConstantClassInfoContainer.ALL_ARGS_CONSTRUCTOR;
import static hygge.util.constant.ConstantClassInfoContainer.BUILDER;
import static hygge.util.constant.ConstantClassInfoContainer.GENERATED;
import static hygge.util.constant.ConstantClassInfoContainer.GETTER;
import static hygge.util.constant.ConstantClassInfoContainer.NO_ARGS_CONSTRUCTOR;
import static hygge.util.constant.ConstantClassInfoContainer.SETTER;

/**
 * 类描述信息
 *
 * @author Xavier
 * @date 2024/3/25
 * @since 1.0
 */
public class ClassInfo {
    protected static final CollectionHelper collectionHelper = UtilCreator.INSTANCE.getDefaultInstance(CollectionHelper.class);
    /**
     * 如果为 {@link Boolean#TRUE}，将认定为 JDK 基础类无需 import
     */
    private boolean isBasic = false;
    /**
     * 类所在包
     */
    private String packageInfo;
    /**
     * 类名
     */
    private String name;
    /**
     * 类修饰符
     */
    private List<Modifier> modifiers = collectionHelper.createCollection(Modifier.PUBLIC);
    /**
     * 类类型
     */
    private ClassType type = ClassType.DEFAULT_CLASS;
    /**
     * 继承的目标类
     */
    private ClassInfo parent;
    /**
     * 类描述
     */
    private String description;
    /**
     * 类注解信息
     */
    private List<ClassInfo> annotations = new ArrayList<>();
    /**
     * 泛型信息(允许元素重复，其 index 信息被外部依赖，例如 Map 首位代表 Key 后一位代表 Value)
     */
    private List<ClassInfo> references = new ArrayList<>();
    /**
     * 类属性
     */
    private List<Property> properties = new ArrayList<>();
    /**
     * 枚举值
     */
    private List<EnumElement> enumElements = new ArrayList<>();

    public ClassInfo init(JavaGeneratorConfiguration configuration) {
        if (configuration.isLombokEnable()) {
            if (type.equals(ClassType.ENUM)) {
                // lombok 的 @Setter 是不支持枚举类的，这也是为什么枚举类开启 lombok 时，生成器仍然需要检查是否添加 setter
                annotations.add(GETTER);

                // 枚举属性如果不可修改，属性要用 final 修饰
                if (!configuration.isEnumPropertyModifiable()) {
                    properties.forEach(item -> item.getModifiers().add(Modifier.FINAL));
                }
            } else if (type.equals(ClassType.DEFAULT_CLASS)) {
                if (modifiers.contains(Modifier.ABSTRACT)) {
                    // 抽象类不需要 Builder 和全参构造方法
                    annotations.add(GETTER);
                    annotations.add(SETTER);
                    annotations.add(GENERATED);
                    annotations.add(NO_ARGS_CONSTRUCTOR);
                } else {
                    annotations.add(GETTER);
                    annotations.add(SETTER);
                    annotations.add(BUILDER);
                    annotations.add(GENERATED);
                    annotations.add(NO_ARGS_CONSTRUCTOR);
                    annotations.add(ALL_ARGS_CONSTRUCTOR);
                }
            }
        }
        return this;
    }

    @JsonIgnore
    public List<ClassInfo> getDependency() {
        HashSet<ClassInfo> resultTemp = new HashSet<>();

        // 继承依赖
        if (parent != null) {
            resultTemp.add(parent);
        }

        // 类注解依赖
        resultTemp.addAll(annotations);

        // 泛型依赖
        references.forEach(item -> {
            resultTemp.add(item);
            resultTemp.addAll(item.getReferences());
        });

        // 类属性依赖
        properties.forEach(item -> {
            resultTemp.add(item.getClassInfo());

            if (!item.getClassInfo().getReferences().isEmpty()) {
                // 泛型中可能存在自己而引发重复
                resultTemp.addAll(item.getClassInfo().getReferences());
            }
        });

        ClassInfo currentClassInfo = this;

        // 依赖信息中排除自己或基础类型
        List<ClassInfo> result = collectionHelper.filterNonemptyItemAsArrayList(false, resultTemp, (item -> {
            if (item.equals(currentClassInfo) || item.isBasic()) {
                return null;
            } else {
                return item;
            }
        }));
        // 依赖需要稳定排序
        result.sort(Comparator.comparing(o -> (o.getPackageInfo() + "." + o.getName())));
        return result;
    }

    public static ClassInfo createListClassInfo(ClassInfo value) {
        return ClassInfo.builder()
                .packageInfo("java.util")
                .name("List")
                .annotations(new ArrayList<>())
                .references(collectionHelper.createCollection(value))
                .properties(new ArrayList<>())
                .build();
    }

    public static ClassInfo createSetClassInfo(ClassInfo value) {
        return ClassInfo.builder()
                .packageInfo("java.util")
                .name("Set")
                .annotations(new ArrayList<>())
                .references(collectionHelper.createCollection(value))
                .properties(new ArrayList<>())
                .build();
    }

    public static ClassInfo createMapClassInfo(ClassInfo key, ClassInfo value) {
        return ClassInfo.builder()
                .packageInfo("java.util")
                .name("Map")
                .annotations(new ArrayList<>())
                .references(collectionHelper.createCollection(key, value))
                .properties(new ArrayList<>())
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClassInfo classInfo = (ClassInfo) o;
        return isBasic == classInfo.isBasic && Objects.equals(packageInfo, classInfo.packageInfo) && Objects.equals(name, classInfo.name) && Objects.equals(modifiers, classInfo.modifiers) && type == classInfo.type && Objects.equals(parent, classInfo.parent) && Objects.equals(description, classInfo.description) && Objects.equals(annotations, classInfo.annotations) && Objects.equals(references, classInfo.references) && Objects.equals(properties, classInfo.properties) && Objects.equals(enumElements, classInfo.enumElements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isBasic, packageInfo, name, modifiers, type, parent, description, annotations, references, properties, enumElements);
    }

    private static boolean $default$isBasic() {
        return false;
    }

    private static List<Modifier> $default$modifiers() {
        return collectionHelper.createCollection(Modifier.PUBLIC);
    }

    private static ClassType $default$type() {
        return ClassType.DEFAULT_CLASS;
    }

    private static List<ClassInfo> $default$annotations() {
        return new ArrayList();
    }

    private static List<ClassInfo> $default$references() {
        return new ArrayList();
    }

    private static List<Property> $default$properties() {
        return new ArrayList();
    }

    private static List<EnumElement> $default$enumElements() {
        return new ArrayList();
    }

    public static ClassInfoBuilder builder() {
        return new ClassInfoBuilder();
    }

    public boolean isBasic() {
        return this.isBasic;
    }

    public String getPackageInfo() {
        return this.packageInfo;
    }

    public String getName() {
        return this.name;
    }

    public List<Modifier> getModifiers() {
        return this.modifiers;
    }

    public ClassType getType() {
        return this.type;
    }

    public ClassInfo getParent() {
        return this.parent;
    }

    public String getDescription() {
        return this.description;
    }

    public List<ClassInfo> getAnnotations() {
        return this.annotations;
    }

    public List<ClassInfo> getReferences() {
        return this.references;
    }

    public List<Property> getProperties() {
        return this.properties;
    }

    public List<EnumElement> getEnumElements() {
        return this.enumElements;
    }

    public void setBasic(boolean isBasic) {
        this.isBasic = isBasic;
    }

    public void setPackageInfo(String packageInfo) {
        this.packageInfo = packageInfo;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setModifiers(List<Modifier> modifiers) {
        this.modifiers = modifiers;
    }

    public void setType(ClassType type) {
        this.type = type;
    }

    public void setParent(ClassInfo parent) {
        this.parent = parent;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAnnotations(List<ClassInfo> annotations) {
        this.annotations = annotations;
    }

    public void setReferences(List<ClassInfo> references) {
        this.references = references;
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }

    public void setEnumElements(List<EnumElement> enumElements) {
        this.enumElements = enumElements;
    }

    public ClassInfo() {
        this.isBasic = $default$isBasic();
        this.modifiers = $default$modifiers();
        this.type = $default$type();
        this.annotations = $default$annotations();
        this.references = $default$references();
        this.properties = $default$properties();
        this.enumElements = $default$enumElements();
    }

    public ClassInfo(boolean isBasic, String packageInfo, String name, List<Modifier> modifiers, ClassType type, ClassInfo parent, String description, List<ClassInfo> annotations, List<ClassInfo> references, List<Property> properties, List<EnumElement> enumElements) {
        this.isBasic = isBasic;
        this.packageInfo = packageInfo;
        this.name = name;
        this.modifiers = modifiers;
        this.type = type;
        this.parent = parent;
        this.description = description;
        this.annotations = annotations;
        this.references = references;
        this.properties = properties;
        this.enumElements = enumElements;
    }

    public static class ClassInfoBuilder {
        private boolean isBasic$set;
        private boolean isBasic$value;
        private String packageInfo;
        private String name;
        private boolean modifiers$set;
        private List<Modifier> modifiers$value;
        private boolean type$set;
        private ClassType type$value;
        private ClassInfo parent;
        private String description;
        private boolean annotations$set;
        private List<ClassInfo> annotations$value;
        private boolean references$set;
        private List<ClassInfo> references$value;
        private boolean properties$set;
        private List<Property> properties$value;
        private boolean enumElements$set;
        private List<EnumElement> enumElements$value;

        ClassInfoBuilder() {
        }

        public ClassInfoBuilder isBasic(boolean isBasic) {
            this.isBasic$value = isBasic;
            this.isBasic$set = true;
            return this;
        }

        public ClassInfoBuilder packageInfo(String packageInfo) {
            this.packageInfo = packageInfo;
            return this;
        }

        public ClassInfoBuilder name(String name) {
            this.name = name;
            return this;
        }

        public ClassInfoBuilder modifiers(List<Modifier> modifiers) {
            this.modifiers$value = modifiers;
            this.modifiers$set = true;
            return this;
        }

        public ClassInfoBuilder type(ClassType type) {
            this.type$value = type;
            this.type$set = true;
            return this;
        }

        public ClassInfoBuilder parent(ClassInfo parent) {
            this.parent = parent;
            return this;
        }

        public ClassInfoBuilder description(String description) {
            this.description = description;
            return this;
        }

        public ClassInfoBuilder annotations(List<ClassInfo> annotations) {
            this.annotations$value = annotations;
            this.annotations$set = true;
            return this;
        }

        public ClassInfoBuilder references(List<ClassInfo> references) {
            this.references$value = references;
            this.references$set = true;
            return this;
        }

        public ClassInfoBuilder properties(List<Property> properties) {
            this.properties$value = properties;
            this.properties$set = true;
            return this;
        }

        public ClassInfoBuilder enumElements(List<EnumElement> enumElements) {
            this.enumElements$value = enumElements;
            this.enumElements$set = true;
            return this;
        }

        public ClassInfo build() {
            boolean isBasic$value = this.isBasic$value;
            if (!this.isBasic$set) {
                isBasic$value = ClassInfo.$default$isBasic();
            }

            List<Modifier> modifiers$value = this.modifiers$value;
            if (!this.modifiers$set) {
                modifiers$value = ClassInfo.$default$modifiers();
            }

            ClassType type$value = this.type$value;
            if (!this.type$set) {
                type$value = ClassInfo.$default$type();
            }

            List<ClassInfo> annotations$value = this.annotations$value;
            if (!this.annotations$set) {
                annotations$value = ClassInfo.$default$annotations();
            }

            List<ClassInfo> references$value = this.references$value;
            if (!this.references$set) {
                references$value = ClassInfo.$default$references();
            }

            List<Property> properties$value = this.properties$value;
            if (!this.properties$set) {
                properties$value = ClassInfo.$default$properties();
            }

            List<EnumElement> enumElements$value = this.enumElements$value;
            if (!this.enumElements$set) {
                enumElements$value = ClassInfo.$default$enumElements();
            }

            return new ClassInfo(isBasic$value, this.packageInfo, this.name, modifiers$value, type$value, this.parent, this.description, annotations$value, references$value, properties$value, enumElements$value);
        }

        public String toString() {
            return "ClassInfo.ClassInfoBuilder(isBasic$value=" + this.isBasic$value + ", packageInfo=" + this.packageInfo + ", name=" + this.name + ", modifiers$value=" + this.modifiers$value + ", type$value=" + this.type$value + ", parent=" + this.parent + ", description=" + this.description + ", annotations$value=" + this.annotations$value + ", references$value=" + this.references$value + ", properties$value=" + this.properties$value + ", enumElements$value=" + this.enumElements$value + ")";
        }
    }
}
