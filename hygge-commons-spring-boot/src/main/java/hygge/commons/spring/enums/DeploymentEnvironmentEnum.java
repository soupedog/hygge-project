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

package hygge.commons.spring.enums;

/**
 * 部署环境
 *
 * @author Xavier
 * @date 2022/6/28
 * @since 1.0
 */
public enum DeploymentEnvironmentEnum {
    /**
     * Development
     */
    DEV(0, "DEV"),
    /**
     * System Integrate Test
     */
    SIT(100, "SIT"),
    /**
     * User Acceptance Test
     */
    UAT(200, "UAT"),
    /**
     * Simulation
     */
    SIM(300, "SIM"),
    /**
     * Production
     */
    PROD(888, "PROD");

    DeploymentEnvironmentEnum(int privilegeLevel, String description) {
        this.privilegeLevel = privilegeLevel;
        this.description = description;
    }

    /**
     * 环境权限值
     */
    private final int privilegeLevel;
    /**
     * 描述
     */
    private final String description;

    public static DeploymentEnvironmentEnum parse(int privilegeLevel) {
        switch (privilegeLevel) {
            case 0:
                return DEV;
            case 100:
                return SIT;
            case 200:
                return UAT;
            case 300:
                return SIM;
            case 888:
                return PROD;
            default:
                throw new IllegalArgumentException(String.format("%s fall to resolve privilegeLevel of %d.", DeploymentEnvironmentEnum.class.getName(), privilegeLevel));
        }
    }

    public int getPrivilegeLevel() {
        return privilegeLevel;
    }

    public String getDescription() {
        return description;
    }
}
