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

package hygge.web.template;

/**
 * 请求的默认返回对象
 *
 * @author Xavier
 * @date 2022/7/5
 * @since 1.0
 */
public class HyggeControllerResponse<C, T> {
    /**
     * 自定义业务码
     */
    private C code;
    /**
     * 提示信息
     */
    private String msg;
    /**
     * 返回核心内容
     */
    private T main;

    public C getCode() {
        return code;
    }

    public void setCode(C code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getMain() {
        return main;
    }

    public void setMain(T main) {
        this.main = main;
    }
}
