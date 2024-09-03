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

package hygge.util.constant;

import hygge.util.generator.java.bo.ClassInfo;
import hygge.util.generator.java.bo.ClassType;

/**
 * 常用类信息容器
 *
 * @author Xavier
 * @date 2024/3/25
 * @since 1.0
 */
@SuppressWarnings({"java:S1192"})
public class ConstantClassInfoContainer {
    public static final ClassInfo BYTE_ARRAY = ClassInfo.builder().isBasic(true).packageInfo("").name("byte[]").build();
    public static final ClassInfo STRING = ClassInfo.builder().isBasic(true).packageInfo("java.lang").name("String").build();
    public static final ClassInfo BYTE = ClassInfo.builder().isBasic(true).packageInfo("java.lang").name("Byte").build();
    public static final ClassInfo SHORT = ClassInfo.builder().isBasic(true).packageInfo("java.lang").name("Short").build();
    public static final ClassInfo INTEGER = ClassInfo.builder().isBasic(true).packageInfo("java.lang").name("Integer").build();
    public static final ClassInfo LONG = ClassInfo.builder().isBasic(true).packageInfo("java.lang").name("Long").build();
    public static final ClassInfo FLOAT = ClassInfo.builder().isBasic(true).packageInfo("java.lang").name("Float").build();
    public static final ClassInfo DOUBLE = ClassInfo.builder().isBasic(true).packageInfo("java.lang").name("Double").build();
    public static final ClassInfo BOOLEAN = ClassInfo.builder().isBasic(true).packageInfo("java.lang").name("Boolean").build();
    public static final ClassInfo BIG_DECIMAL = ClassInfo.builder().packageInfo("java.math").name("BigDecimal").build();
    public static final ClassInfo LOCAL_DATE_TIME = ClassInfo.builder().packageInfo("java.time").name("LocalDateTime").build();
    public static final ClassInfo ZONED_DATE_TIME = ClassInfo.builder().packageInfo("java.time").name("ZonedDateTime").build();
    public static final ClassInfo OFFSET_DATE_TIME = ClassInfo.builder().packageInfo("java.time").name("OffsetDateTime").build();
    public static final ClassInfo TIMESTAMP = ClassInfo.builder().packageInfo("java.sql").name("Timestamp").build();
    public static final ClassInfo GETTER = ClassInfo.builder().packageInfo("lombok").name("Getter").type(ClassType.ANNOTATION).build();
    public static final ClassInfo SETTER = ClassInfo.builder().packageInfo("lombok").name("Setter").type(ClassType.ANNOTATION).build();
    public static final ClassInfo BUILDER = ClassInfo.builder().packageInfo("lombok").name("Builder").type(ClassType.ANNOTATION).build();
    public static final ClassInfo GENERATED = ClassInfo.builder().packageInfo("lombok").name("Generated").type(ClassType.ANNOTATION).build();
    public static final ClassInfo NO_ARGS_CONSTRUCTOR = ClassInfo.builder().packageInfo("lombok").name("NoArgsConstructor").type(ClassType.ANNOTATION).build();
    public static final ClassInfo ALL_ARGS_CONSTRUCTOR = ClassInfo.builder().packageInfo("lombok").name("AllArgsConstructor").type(ClassType.ANNOTATION).build();
}
