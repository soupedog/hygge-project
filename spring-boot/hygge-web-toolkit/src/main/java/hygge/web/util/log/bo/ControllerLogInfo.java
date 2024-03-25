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

package hygge.web.util.log.bo;


import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import hygge.util.json.jackson.serializer.HyggeLogInfoSerializer;
import hygge.web.util.log.enums.ControllerLogType;

/**
 * Controller 层自动日志对象
 *
 * @author Xavier
 * @date 2022/7/14
 * @since 1.0
 */
public class ControllerLogInfo {
    private ControllerLogType type;
    private String path;
    @JsonSerialize(using = HyggeLogInfoSerializer.class)
    private Object input;
    @JsonSerialize(using = HyggeLogInfoSerializer.class)
    private Object output;
    private String errorMessage;
    /**
     * 处理耗时(ms)
     */
    private Long cost;

    public ControllerLogType getType() {
        return type;
    }

    public void setType(ControllerLogType type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Object getInput() {
        return input;
    }

    public void setInput(Object input) {
        this.input = input;
    }

    public Object getOutput() {
        return output;
    }

    public void setOutput(Object output) {
        this.output = output;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Long getCost() {
        return cost;
    }

    public void setCost(Long cost) {
        this.cost = cost;
    }
}
