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

package example.hygge.controller;

import hygge.util.bo.configuration.arranger.PropertiesArrangerConfiguration;
import hygge.util.impl.PropertiesConfigurationArranger;
import hygge.util.template.HyggeJsonUtilContainer;

import java.io.File;
import java.util.StringJoiner;

/**
 * 演示配置项整理工具用法
 *
 * @author Xavier
 * @date 2024/10/6
 * @since 1.0
 */
public class OrganizeConfigurationExample extends HyggeJsonUtilContainer {
    public static void main(String[] args) {
        String absolutePath = new StringJoiner(File.separator)
                .add(System.getProperty("user.dir"))
                .add("web-application-example")
                .add("src")
                .add("main")
                .add("resources")
                .toString();

        PropertiesArrangerConfiguration configuration = new PropertiesArrangerConfiguration(
                // 指定这两个配置项优先展示
                collectionHelper.createCollection("spring.profiles.active", "spring.application.name")
        );
        configuration.setSkipFileNameList(collectionHelper.createCollection("application.properties"));

        new PropertiesConfigurationArranger().organizePropertiesFiles(absolutePath, configuration);
    }
}
