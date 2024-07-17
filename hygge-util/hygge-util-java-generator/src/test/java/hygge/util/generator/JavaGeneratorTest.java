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

package hygge.util.generator;


import hygge.commons.constant.ConstantParameters;
import hygge.util.UtilCreator;
import hygge.util.constant.ConstantClassInfoContainer;
import hygge.util.definition.FileHelper;
import hygge.util.generator.java.JavaGenerator;
import hygge.util.generator.java.bo.ClassInfo;
import hygge.util.generator.java.bo.ClassType;
import hygge.util.generator.java.bo.EnumElement;
import hygge.util.generator.java.bo.JavaGeneratorConfiguration;
import hygge.util.generator.java.bo.Modifier;
import hygge.util.generator.java.bo.Property;
import hygge.util.template.HyggeJsonUtilContainer;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.StringJoiner;

class JavaGeneratorTest extends HyggeJsonUtilContainer {
    private static final FileHelper fileHelper = UtilCreator.INSTANCE.getDefaultInstance(FileHelper.class);
    private static final JavaGeneratorConfiguration JAVA_GENERATOR_CONFIGURATION = new JavaGeneratorConfiguration();

    private static final StringJoiner joinerForDefault = new StringJoiner(ConstantParameters.FILE_SEPARATOR)
            .add("src")
            .add("test")
            .add("java")
            .add("hygge")
            .add("util")
            .add("generator")
            .add("opt");

    private static final String absolutePathForDefault = System.getProperty("user.dir") + ConstantParameters.FILE_SEPARATOR + joinerForDefault;
    private static final StringJoiner joinerForEnums = new StringJoiner(ConstantParameters.FILE_SEPARATOR)
            .add("src")
            .add("test")
            .add("java")
            .add("hygge")
            .add("util")
            .add("generator")
            .add("opt")
            .add("enums");

    private static final String absolutePathForEnums = System.getProperty("user.dir") + ConstantParameters.FILE_SEPARATOR + joinerForEnums;
    private static ClassInfo classInfo;
    private static ClassInfo enumClassInfo;

    @BeforeAll
    static void initClass() {
        enumClassInfo = ClassInfo.builder()
                .name("SexEnum")
                .type(ClassType.ENUM)
                .description("枚举简单示例")
                .packageInfo("hygge.util.generator.opt.enums")
                .properties(collectionHelper.createCollection(
                        Property.builder()
                                .name("value")
                                .classInfo(ConstantClassInfoContainer.INTEGER)
                                .description("枚举数字值")
                                .build()
                        ,
                        Property.builder()
                                .name("text")
                                .classInfo(ConstantClassInfoContainer.STRING)
                                .description("枚举文本值")
                                .build()

                ))
                .enumElements(collectionHelper.createCollection(
                        EnumElement.builder()
                                .name("SECRET")
                                .description("保密")
                                .params(collectionHelper.createCollection(
                                        0,
                                        "密"
                                ))
                                .build(),
                        EnumElement.builder()
                                .name("MALE")
                                .description("男性")
                                .params(collectionHelper.createCollection(
                                        100,
                                        "男"
                                ))
                                .build(),
                        EnumElement.builder()
                                .name("FEMALE")
                                .description("女性")
                                .params(collectionHelper.createCollection(
                                        200,
                                        "女"
                                ))
                                .build()
                ))
                .build().init(JAVA_GENERATOR_CONFIGURATION);

        classInfo = ClassInfo.builder()
                .name("User")
                .description("简单示例")
                .packageInfo("hygge.util.generator.opt")
                .properties(collectionHelper.createCollection(
                        Property.builder()
                                .name("name")
                                .classInfo(ConstantClassInfoContainer.STRING)
                                .description("用户名称")
                                .build()
                        ,
                        Property.builder()
                                .name("age")
                                .classInfo(ConstantClassInfoContainer.BYTE)
                                .build()
                        ,
                        Property.builder()
                                .name("balance")
                                .classInfo(ConstantClassInfoContainer.BIG_DECIMAL)
                                .build()
                        ,
                        Property.builder()
                                .name("debt")
                                .classInfo(ClassInfo.createMapClassInfo(ConstantClassInfoContainer.STRING, ConstantClassInfoContainer.BIG_DECIMAL))
                                .build()
                        ,
                        Property.builder()
                                .name("file")
                                .classInfo(ConstantClassInfoContainer.BYTE_ARRAY)
                                .build()
                ))
                .build()
                .init(JAVA_GENERATOR_CONFIGURATION);

        classInfo.getProperties().add(Property.builder()
                .name("friends")
                .classInfo(ClassInfo.createListClassInfo(classInfo))
                .build()
        );

        classInfo.getProperties().add(Property.builder()
                .name("sex")
                .classInfo(enumClassInfo)
                .build()
        );

        classInfo.getModifiers().add(Modifier.ABSTRACT);
        classInfo.getProperties().forEach(item -> item.setModifiers(Modifier.createModifierList(Modifier.PROTECTED)));
    }

    @Test
    void getDefaultContentTest() {
        StringBuilder content = JavaGenerator.getDefaultContent(JAVA_GENERATOR_CONFIGURATION, classInfo);

        System.out.println(absolutePathForDefault);

        System.out.println(content);

        fileHelper.saveTextFile(absolutePathForDefault, classInfo.getName(), ".java", content.toString());
    }

    @Test
    void getEnumContentTest() {
        StringBuilder content = JavaGenerator.getEnumContent(JAVA_GENERATOR_CONFIGURATION, enumClassInfo);

        System.out.println(jsonHelper.formatAsString(enumClassInfo));
        System.out.println();
        System.out.println(content);

        fileHelper.saveTextFile(absolutePathForEnums, enumClassInfo.getName(), ".java", content.toString());
    }
}