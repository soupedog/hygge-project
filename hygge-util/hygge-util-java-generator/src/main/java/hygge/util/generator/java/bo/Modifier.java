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

import java.util.List;

public enum Modifier {

    DEFAULT("", 0),
    PUBLIC("public", 0),
    PROTECTED("protected", 0),
    PRIVATE("private", 0),
    ABSTRACT("abstract", 10),
    STATIC("static", 20),
    FINAL("final", 30),
    ;
    private static final CollectionHelper collectionHelper = UtilCreator.INSTANCE.getDefaultInstance(CollectionHelper.class);

    private final String value;
    private final int order;

    Modifier(String value, int order) {
        this.value = value;
        this.order = order;
    }

    public static List<Modifier> createModifierList(Modifier... modifiers) {
        return collectionHelper.createCollection(modifiers);
    }

    public String getValue() {
        return value;
    }

    public int getOrder() {
        return order;
    }
}
