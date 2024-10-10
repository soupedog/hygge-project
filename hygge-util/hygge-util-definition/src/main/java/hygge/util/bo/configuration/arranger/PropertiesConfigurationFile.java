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

package hygge.util.bo.configuration.arranger;

import java.util.List;

/**
 * 配置项文件
 *
 * @author Xavier
 * @date 2024/9/25
 * @since 1.0
 */
public class PropertiesConfigurationFile {
    private String absolutePath;
    /**
     * 该配置项文件名称
     */
    private String fileName;
    /**
     * 基于原始顺序的配置项
     */
    private List<PropertiesConfigurationItem> propertiesConfigurationItemList;

    public String getAbsolutePath() {
        return absolutePath;
    }

    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public List<PropertiesConfigurationItem> getConfigurationItemList() {
        return propertiesConfigurationItemList;
    }

    public void setConfigurationItemList(List<PropertiesConfigurationItem> propertiesConfigurationItemList) {
        this.propertiesConfigurationItemList = propertiesConfigurationItemList;
    }
}
