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

package hygge.web.util.log;

import hygge.commons.template.container.base.AbstractHyggeContext;
import hygge.web.util.log.bo.ControllerLogInfo;

/**
 * ControllerLog 上下文容器
 *
 * @author Xavier
 * @date 2022/7/14
 * @since 1.0
 */
public class ControllerLogContext extends AbstractHyggeContext<ControllerLogContext.Key> {
    private Long startTs;
    private ControllerLogInfo logInfo = new ControllerLogInfo();

    public ControllerLogContext(Long startTs) {
        this.startTs = startTs;
    }

    public Long getStartTs() {
        return startTs;
    }

    public void setStartTs(Long startTs) {
        this.startTs = startTs;
    }

    public ControllerLogInfo getLogInfo() {
        return logInfo;
    }

    public void setLogInfo(ControllerLogInfo logInfo) {
        this.logInfo = logInfo;
    }

    public enum Key {
        RAW_RESPONSE,
        ;
    }
}
