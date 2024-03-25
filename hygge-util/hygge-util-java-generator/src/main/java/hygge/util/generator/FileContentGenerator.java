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

package hygge.util.generator;

import hygge.commons.constant.ConstantParameters;
import hygge.commons.template.definition.NoParameterNoResponseFunction;
import hygge.util.UtilCreator;
import hygge.util.definition.CollectionHelper;
import hygge.util.definition.ParameterHelper;

public abstract class FileContentGenerator extends ConstantParameters {
    protected static final CollectionHelper collectionHelper = UtilCreator.INSTANCE.getDefaultInstance(CollectionHelper.class);
    protected static final ParameterHelper parameterHelper = UtilCreator.INSTANCE.getDefaultInstance(ParameterHelper.class);

    protected static void addOneLine(int indentationLevel, StringBuilder content, String lineContent) {
        addIndentation(content, indentationLevel);
        content.append(lineContent);
        addEmptyLine(content, 1);
    }

    protected static void addOneLine(int indentationLevel, StringBuilder content, NoParameterNoResponseFunction lineContentBuildFunction) {
        addIndentation(content, indentationLevel);
        lineContentBuildFunction.execute();
        addEmptyLine(content, 1);
    }

    protected static void addEmptyLine(StringBuilder content, int times) {
        for (int i = 0; i < times; i++) {
            content.append(LINE_SEPARATOR);
        }
    }

    protected static void deleteEmptyLine(StringBuilder content, int times) {
        parameterHelper.removeStringFormTail(content, LINE_SEPARATOR, times);
    }

    protected static void addIndentation(StringBuilder content, int times) {
        for (int i = 0; i < times; i++) {
            content.append(BLANK_4);
        }
    }

    protected static void deleteIndentation(StringBuilder content, int times) {
        parameterHelper.removeStringFormTail(content, BLANK_4, times);
    }
}
