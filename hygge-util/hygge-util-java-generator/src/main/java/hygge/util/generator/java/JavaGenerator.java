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

package hygge.util.generator.java;

import hygge.util.generator.FileContentGenerator;
import hygge.util.generator.java.bo.ClassInfo;
import hygge.util.generator.java.bo.ClassType;
import hygge.util.generator.java.bo.EnumElement;
import hygge.util.generator.java.bo.JavaGeneratorConfiguration;
import hygge.util.generator.java.bo.Modifier;
import hygge.util.generator.java.bo.Property;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SuppressWarnings({"java:S1192"})
public class JavaGenerator extends FileContentGenerator {
    protected static void addPackage(StringBuilder content, ClassInfo classInfo) {
        addOneLine(0, content, "package " + classInfo.getPackageInfo() + ";");
    }

    protected static void addImport(StringBuilder content, ClassInfo classInfo) {
        addOneLine(0, content, "import " + classInfo.getPackageInfo() + "." + classInfo.getName() + ";");
    }

    protected static void addImportInfo(StringBuilder content, List<ClassInfo> dependencyList) {
        Map<Boolean, List<ClassInfo>> map = dependencyList.stream().collect(Collectors.partitioningBy(item -> item.getPackageInfo().startsWith("java.")));

        List<ClassInfo> internal = map.get(Boolean.TRUE);
        List<ClassInfo> thirdParty = map.get(Boolean.FALSE);

        thirdParty.forEach(item -> addImport(content, item));
        if (!thirdParty.isEmpty()) {
            addEmptyLine(content, 1);
        }
        internal.forEach(item -> addImport(content, item));
        if (!internal.isEmpty()) {
            addEmptyLine(content, 1);
        }
    }

    protected static void addClassDocInfo(int indentationLevel, StringBuilder content, JavaGeneratorConfiguration configuration, ClassInfo classInfo) {
        addOneLine(indentationLevel, content, "/**");
        addOneLine(indentationLevel, content, " * " + classInfo.getDescription());
        addOneLine(indentationLevel, content, " * ");
        addOneLine(indentationLevel, content, " * @author " + configuration.getAuthor());
        addOneLine(indentationLevel, content, " * @date " + configuration.getDate());
        addOneLine(indentationLevel, content, " */");
    }

    protected static void addClassAnnotationInfo(int indentationLevel, StringBuilder content, ClassInfo classInfo) {
        classInfo.getAnnotations().forEach(item -> addOneLine(indentationLevel, content, "@" + item.getName()));
    }

    protected static void addClassStartLine(int indentationLevel, StringBuilder content, ClassInfo classInfo) {
        addOneLine(indentationLevel, content, () -> {
            classInfo.getModifiers().forEach(item -> {
                content.append(item.getValue());
                content.append(" ");
            });

            content.append(classInfo.getType().getVale());
            content.append(" ");
            content.append(classInfo.getName());
            content.append(" ");

            if (classInfo.getParent() != null) {
                content.append("extends");
                content.append(" ");
                content.append(classInfo.getParent().getName());
                content.append(" ");
            }
            content.append("{");
        });
    }

    private static void addAllArgsConstructor(int indentationLevel, StringBuilder content, ClassInfo classInfo) {
        addOneLine(indentationLevel + 1, content, () -> {
            content.append(classInfo.getName());
            content.append("(");
            classInfo.getProperties().forEach(item -> {
                content.append(item.getClassInfo().getName());
                content.append(" ");
                content.append(item.getName());
                content.append(",");
                content.append(" ");
            });
            parameterHelper.removeStringFormTail(content, " ", 1);
            parameterHelper.removeStringFormTail(content, ",", 1);
            content.append(") {");
        });

        classInfo.getProperties().forEach(item -> addOneLine(indentationLevel + 2, content, String.format("this.%s = %s;", item.getName(), item.getName())));

        addOneLine(indentationLevel + 1, content, "}");
    }

    protected static void addProperty(int indentationLevel, StringBuilder content, Property property) {
        // java doc
        if (property.getDescription() != null && !property.getDescription().isEmpty()) {
            addOneLine(indentationLevel + 1, content, "/**");
            addOneLine(indentationLevel + 1, content, " * " + property.getDescription());
            addOneLine(indentationLevel + 1, content, " */");
        }

        addOneLine(indentationLevel + 1, content, () -> {
            property.getModifiers().forEach(item -> {
                content.append(item.getValue());
                content.append(" ");
            });

            content.append(property.formatClassAsString());

            content.append(" ");

            content.append(property.getName());
            content.append(";");
        });
    }


    protected static void addGetSetFunction(JavaGeneratorConfiguration configuration, int indentationLevel, StringBuilder content, ClassInfo classInfo) {
        // 没开启 lombok 时
        if (!configuration.isLombokEnable()) {
            addEmptyLine(content, 1);

            classInfo.getProperties().forEach(property -> {
                addGetFunction(indentationLevel, content, property);
                addEmptyLine(content, 1);

                if (!property.getModifiers().contains(Modifier.FINAL)) {
                    addSetFunction(indentationLevel, content, property);
                    addEmptyLine(content, 1);
                }
            });

            deleteEmptyLine(content, 1);
        } else if (ClassType.ENUM.equals(classInfo.getType())) {
            // 开启 lombok 但是枚举类型时，需要检查是否需要 setter
            addEmptyLine(content, 1);

            classInfo.getProperties().forEach(property -> {
                if (!property.getModifiers().contains(Modifier.FINAL)) {
                    addSetFunction(indentationLevel, content, property);
                    addEmptyLine(content, 1);
                }
            });

            deleteEmptyLine(content, 1);
        }
    }

    protected static void addGetFunction(int indentationLevel, StringBuilder content, Property property) {
        addOneLine(indentationLevel + 1, content, String.format("public %s get%s() {", property.formatClassAsString(), parameterHelper.upperCaseFirstLetter(property.getName())));
        addOneLine(indentationLevel + 2, content, String.format("return %s;", property.getName()));
        addOneLine(indentationLevel + 1, content, "}");
    }

    private static void addSetFunction(int indentationLevel, StringBuilder content, Property property) {
        addOneLine(indentationLevel + 1, content, String.format("public void set%s(%s %s) {", parameterHelper.upperCaseFirstLetter(property.getName()), property.formatClassAsString(), property.getName()));
        addOneLine(indentationLevel + 2, content, String.format("this.%s = %s;", property.getName(), property.getName()));
        addOneLine(indentationLevel + 1, content, "}");
    }

    public static StringBuilder getDefaultContent(JavaGeneratorConfiguration configuration, ClassInfo classInfo) {
        int rootIndentationLevel = 0;

        StringBuilder content = new StringBuilder();

        addPackage(content, classInfo);

        addEmptyLine(content, 1);

        List<ClassInfo> dependencyList = classInfo.getDependency();

        addImportInfo(content, dependencyList);

        addClassDocInfo(rootIndentationLevel, content, configuration, classInfo);

        addClassAnnotationInfo(rootIndentationLevel, content, classInfo);

        addClassStartLine(rootIndentationLevel, content, classInfo);

        List<Property> properties = classInfo.getProperties();

        properties.forEach(item -> addProperty(rootIndentationLevel, content, item));

        addGetSetFunction(configuration, rootIndentationLevel, content, classInfo);

        addOneLine(rootIndentationLevel, content, "}");
        return content;
    }

    public static StringBuilder getEnumContent(JavaGeneratorConfiguration configuration, ClassInfo classInfo) {
        int rootIndentationLevel = 0;

        StringBuilder content = new StringBuilder();

        addPackage(content, classInfo);

        addEmptyLine(content, 1);

        List<ClassInfo> dependencyList = classInfo.getDependency();

        addImportInfo(content, dependencyList);

        addClassDocInfo(rootIndentationLevel, content, configuration, classInfo);

        addClassAnnotationInfo(rootIndentationLevel, content, classInfo);

        addClassStartLine(rootIndentationLevel, content, classInfo);

        addEnumElement(rootIndentationLevel, content, classInfo);

        addEmptyLine(content, 1);

        addAllArgsConstructor(rootIndentationLevel, content, classInfo);

        addEmptyLine(content, 1);

        List<Property> properties = classInfo.getProperties();

        properties.forEach(item -> addProperty(rootIndentationLevel, content, item));

        addGetSetFunction(configuration, rootIndentationLevel, content, classInfo);

        addEnumParseFunction(rootIndentationLevel, content, classInfo);

        addOneLine(rootIndentationLevel, content, "}");
        return content;
    }

    protected static void addEnumParseFunction(int indentationLevel, StringBuilder content, ClassInfo classInfo) {
        // ParseFunction 1
        addEmptyLine(content, 1);
        addOneLine(indentationLevel + 1, content, "/**");
        addOneLine(indentationLevel + 1, content, " * 根据数字获取对应的枚举");
        addOneLine(indentationLevel + 1, content, " *");
        addOneLine(indentationLevel + 1, content, " * @throws IllegalArgumentException 当入参为 null 或与枚举匹配失败时");
        addOneLine(indentationLevel + 1, content, " */");

        addOneLine(indentationLevel + 1, content, String.format("public static %s parse(Integer target) {", classInfo.getName()));
        addOneLine(indentationLevel + 2, content, "if (target == null) {");
        addOneLine(indentationLevel + 3, content, String.format("throw new IllegalArgumentException(\"Unexpected value of %s, it can't be null.\");", classInfo.getName()));
        addOneLine(indentationLevel + 2, content, "}");

        addOneLine(indentationLevel + 2, content, "switch (target) {");
        for (int i = 0; i < classInfo.getEnumElements().size(); i++) {
            EnumElement enumElement = classInfo.getEnumElements().get(i);
            addOneLine(indentationLevel + 3, content, "case " + enumElement.formatParam(enumElement.getParams().get(0)) + ":");
            addOneLine(indentationLevel + 4, content, "return " + classInfo.getName() + "." + enumElement.getName() + ";");
        }
        addOneLine(indentationLevel + 3, content, "default:");
        addOneLine(indentationLevel + 4, content, String.format("throw new IllegalArgumentException(\"Unexpected value of %s, it can't be \" + target + \".\");", classInfo.getName()));
        addOneLine(indentationLevel + 2, content, "}");

        addOneLine(indentationLevel + 1, content, "}");

        // ParseFunction 2
        addEmptyLine(content, 1);
        addOneLine(indentationLevel + 1, content, "/**");
        addOneLine(indentationLevel + 1, content, " * 根据数字获取对应的枚举");
        addOneLine(indentationLevel + 1, content, " *");
        addOneLine(indentationLevel + 1, content, " * @param defaultValue 入参为 null 或转换发生异常时，强制返回的转换结果");
        addOneLine(indentationLevel + 1, content, " */");

        addOneLine(indentationLevel + 1, content, String.format("public static %s parse(Integer target, %s defaultValue) {", classInfo.getName(), classInfo.getName()));
        addOneLine(indentationLevel + 2, content, "if (target == null) {");
        addOneLine(indentationLevel + 3, content, "return defaultValue;");
        addOneLine(indentationLevel + 2, content, "}");

        addOneLine(indentationLevel + 2, content, "switch (target) {");
        for (int i = 0; i < classInfo.getEnumElements().size(); i++) {
            EnumElement enumElement = classInfo.getEnumElements().get(i);
            addOneLine(indentationLevel + 3, content, "case " + enumElement.formatParam(enumElement.getParams().get(0)) + ":");
            addOneLine(indentationLevel + 4, content, "return " + classInfo.getName() + "." + enumElement.getName() + ";");
        }
        addOneLine(indentationLevel + 3, content, "default:");
        addOneLine(indentationLevel + 4, content, "return defaultValue;");
        addOneLine(indentationLevel + 2, content, "}");

        addOneLine(indentationLevel + 1, content, "}");

        // ParseFunction 3
        addEmptyLine(content, 1);
        addOneLine(indentationLevel + 1, content, "/**");
        addOneLine(indentationLevel + 1, content, " * 根据枚举 text 或 name 值获取对应的枚举");
        addOneLine(indentationLevel + 1, content, " *");
        addOneLine(indentationLevel + 1, content, " * @throws IllegalArgumentException 当入参为 null 或与枚举匹配失败时");
        addOneLine(indentationLevel + 1, content, " */");

        addOneLine(indentationLevel + 1, content, String.format("public static %s parse(String target) {", classInfo.getName()));
        addOneLine(indentationLevel + 2, content, "if (target == null) {");
        addOneLine(indentationLevel + 3, content, String.format("return %s.valueOf(null);", classInfo.getName()));
        addOneLine(indentationLevel + 2, content, "}");

        addOneLine(indentationLevel + 2, content, "switch (target) {");
        for (int i = 0; i < classInfo.getEnumElements().size(); i++) {
            EnumElement enumElement = classInfo.getEnumElements().get(i);
            addOneLine(indentationLevel + 3, content, "case " + enumElement.formatParam(enumElement.getParams().get(1)) + ":");
            addOneLine(indentationLevel + 4, content, "return " + classInfo.getName() + "." + enumElement.getName() + ";");
        }
        addOneLine(indentationLevel + 3, content, "default:");
        addOneLine(indentationLevel + 4, content, String.format("return %s.valueOf(target);", classInfo.getName()));
        addOneLine(indentationLevel + 2, content, "}");

        addOneLine(indentationLevel + 1, content, "}");

        // ParseFunction 4
        addEmptyLine(content, 1);
        addOneLine(indentationLevel + 1, content, "/**");
        addOneLine(indentationLevel + 1, content, " * 根据枚举 text 或 name 值获取对应的枚举");
        addOneLine(indentationLevel + 1, content, " *");
        addOneLine(indentationLevel + 1, content, " * @param defaultValue 入参为 null 或转换发生异常时，强制返回的转换结果");
        addOneLine(indentationLevel + 1, content, " */");

        addOneLine(indentationLevel + 1, content, String.format("public static %s parse(String target, %s defaultValue) {", classInfo.getName(), classInfo.getName()));
        addOneLine(indentationLevel + 2, content, "if (target == null) {");
        addOneLine(indentationLevel + 3, content, "return defaultValue;");
        addOneLine(indentationLevel + 2, content, "}");

        addOneLine(indentationLevel + 2, content, "switch (target) {");
        for (int i = 0; i < classInfo.getEnumElements().size(); i++) {
            EnumElement enumElement = classInfo.getEnumElements().get(i);
            addOneLine(indentationLevel + 3, content, "case " + enumElement.formatParam(enumElement.getParams().get(1)) + ":");
            addOneLine(indentationLevel + 4, content, "return " + classInfo.getName() + "." + enumElement.getName() + ";");
        }
        addOneLine(indentationLevel + 3, content, "default:");
        addOneLine(indentationLevel + 4, content, "try {");
        addOneLine(indentationLevel + 5, content, String.format("return %s.valueOf(target);", classInfo.getName()));
        addOneLine(indentationLevel + 4, content, "} catch (IllegalArgumentException e) {");
        addOneLine(indentationLevel + 5, content, "return defaultValue;");
        addOneLine(indentationLevel + 4, content, "}");
        addOneLine(indentationLevel + 2, content, "}");

        addOneLine(indentationLevel + 1, content, "}");
    }

    protected static void addEnumElement(int indentationLevel, StringBuilder content, ClassInfo classInfo) {
        classInfo.getEnumElements().forEach(item -> {
            // java doc
            addOneLine(indentationLevel + 1, content, "/**");
            addOneLine(indentationLevel + 1, content, " * " + item.getDescription());
            addOneLine(indentationLevel + 1, content, " */");

            // 枚举值
            addOneLine(indentationLevel + 1, content, () -> {
                content.append(item.getName());
                content.append("(");

                item.getParams().forEach(param -> {
                    content.append(item.formatParam(param));
                    content.append(",");
                    content.append(" ");
                });

                parameterHelper.removeStringFormTail(content, " ", 1);
                parameterHelper.removeStringFormTail(content, ",", 1);
                content.append("),");
            });
        });

        addOneLine(indentationLevel + 1, content, ";");
    }
}
