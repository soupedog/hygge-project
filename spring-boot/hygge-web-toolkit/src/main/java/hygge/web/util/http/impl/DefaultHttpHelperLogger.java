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

package hygge.web.util.http.impl;

import hygge.web.util.http.bo.HttpResponse;
import hygge.web.util.http.configuration.HttpHelperRequestConfiguration;
import hygge.web.util.http.definition.HttpHelperLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static hygge.web.util.http.configuration.HttpHelperConfiguration.HttpLogType;

/**
 * HttpHelper 默认日志记录器
 *
 * @author Xavier
 * @date 2022/7/12
 * @since 1.0
 */
public class DefaultHttpHelperLogger implements HttpHelperLogger {
    private static final Logger log = LoggerFactory.getLogger(DefaultHttpHelperLogger.class);
    private static final String LOG_PREFIX = "HttpHelper :";
    /**
     * byte → mb 进制
     */
    public static final BigDecimal byteToMb = new BigDecimal(1048576);

    @Override
    public void logOutput(HttpHelperRequestConfiguration config, HttpResponse<?, ?> result) {
        if (HttpLogType.NONE.equals(config.logType())) {
            return;
        }

        String logInfo = null;
        HttpResponse<Object, Object> resultTemp = (HttpResponse<Object, Object>) result;

        Long rawStartTs = resultTemp.getStartTs();
        resultTemp.setStartTs(null);

        HttpHeaders rawRequestHeaders = resultTemp.getRequestHeaders();
        HttpHeaders rawResponseHeaders = resultTemp.getResponseHeaders();

        Object rawRequestData = resultTemp.getRequestData();
        if (rawRequestData instanceof byte[]) {
            BigDecimal fileSizeByte = new BigDecimal(((byte[]) rawRequestData).length);
            resultTemp.setRequestData("byte[] ≈ " + fileSizeByte.divide(byteToMb, 3, RoundingMode.FLOOR).toPlainString() + " MB");
        }

        Object rawData = resultTemp.getData();
        if (rawData instanceof byte[]) {
            BigDecimal fileSizeByte = new BigDecimal(((byte[]) rawData).length);
            resultTemp.setData("byte[] ≈ " + fileSizeByte.divide(byteToMb, 3, RoundingMode.FLOOR).toPlainString() + " MB");
        }

        resultTemp.setCost(System.currentTimeMillis() - rawStartTs);

        switch (config.logType()) {
            case STANDARD:
                logInfo = LOG_PREFIX + resultTemp;
                break;
            case NO_BODY:
                resultTemp.setRequestData(null);
                resultTemp.setData(null);
                logInfo = LOG_PREFIX + resultTemp;
                resultTemp.setRequestData(rawRequestData);
                resultTemp.setData(rawData);
                break;
            case NO_HEADERS:
                resultTemp.setRequestHeaders(null);
                resultTemp.setResponseHeaders(null);
                logInfo = LOG_PREFIX + resultTemp;
                resultTemp.setRequestHeaders(rawRequestHeaders);
                resultTemp.setResponseHeaders(rawResponseHeaders);
                break;
            case NO_RESPONSE_HEADERS:
                resultTemp.setResponseHeaders(null);
                logInfo = LOG_PREFIX + resultTemp;
                resultTemp.setResponseHeaders(rawResponseHeaders);
                break;
            case NO_HEADERS_BODY:
                resultTemp.setRequestHeaders(null);
                resultTemp.setResponseHeaders(null);
                resultTemp.setRequestData(null);
                resultTemp.setData(null);
                logInfo = LOG_PREFIX + resultTemp;
                resultTemp.setRequestHeaders(rawRequestHeaders);
                resultTemp.setResponseHeaders(rawResponseHeaders);
                resultTemp.setRequestData(rawRequestData);
                resultTemp.setData(rawData);
                break;
            default:
                // 不可能触发
        }

        // 原始数据重新回填
        resultTemp.setStartTs(rawStartTs);
        resultTemp.setData(rawData);

        if (Boolean.TRUE.equals(resultTemp.getExceptionOccurred())) {
            log.warn(logInfo);
        } else {
            log.info(logInfo);
        }
    }
}
