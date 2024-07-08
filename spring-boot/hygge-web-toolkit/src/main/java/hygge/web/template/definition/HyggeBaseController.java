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

package hygge.web.template.definition;

import hygge.commons.constant.enums.GlobalHyggeCodeEnum;
import hygge.commons.exception.ExternalException;
import hygge.commons.exception.ExternalRuntimeException;
import hygge.commons.exception.InternalException;
import hygge.commons.exception.InternalRuntimeException;
import hygge.commons.exception.LightException;
import hygge.commons.exception.LightRuntimeException;
import hygge.commons.exception.ParameterException;
import hygge.commons.exception.ParameterRuntimeException;
import hygge.commons.exception.UtilException;
import hygge.commons.exception.UtilRuntimeException;
import hygge.commons.exception.main.HyggeException;
import hygge.commons.exception.main.HyggeRuntimeException;
import hygge.commons.template.definition.HyggeCode;
import hygge.commons.template.definition.HyggeInfo;
import hygge.web.template.HyggeControllerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Hygge Controller 基础功能接口
 *
 * @author Xavier
 * @date 2024/7/3
 * @since 1.0
 */
@ControllerAdvice
public interface HyggeBaseController<R extends ResponseEntity<?>> {
    Logger log = LoggerFactory.getLogger(HyggeController.class);

    /**
     * 便用 entity 直接返回不进行包装的示例模版
     */
    HyggeControllerResponseWrapper<ResponseEntity<?>> emptyResponseWrapper = (httpStatus, headers, hyggeCode, msg, entity, throwable) -> {
        // "status(HttpStatus status)" 方法在 Spring Boot 3.x 环境中不存在了，此处用 int 型重载进行兼容性优化
        ResponseEntity.BodyBuilder builder = ResponseEntity.status(httpStatus.value());

        ResponseEntity<?> response;
        if (headers != null) {
            response = builder.headers(headers).body(entity);
        } else {
            response = builder.body(entity);
        }

        return response;
    };

    /**
     * 非严重异常日志的简易开关(默认为 true ：进行打印)
     *
     * @param exception 异常信息
     * @return 是否打印非严重异常日志
     */
    default boolean printNonSeriousExceptionLog(HyggeInfo exception) {
        return true;
    }

    @ExceptionHandler({
            ExternalException.class,
            InternalException.class,
            UtilException.class,
            LightException.class,
            ParameterException.class
    })
    default R hyggeExceptionHandler(HyggeException e) {
        if (e.getHyggeCode().isSerious()) {
            log.error(e.getMessage(), e);
            return fail(HttpStatus.INTERNAL_SERVER_ERROR, e);
        } else {
            if (printNonSeriousExceptionLog(e)) {
                log.warn(e.getMessage(), e);
            }
            return fail(HttpStatus.BAD_REQUEST, e);
        }
    }

    @ExceptionHandler({
            ExternalRuntimeException.class,
            InternalRuntimeException.class,
            UtilRuntimeException.class,
            LightRuntimeException.class,
            ParameterRuntimeException.class
    })
    default R hyggeRuntimeExceptionHandler(HyggeRuntimeException e) {
        if (e.getHyggeCode().isSerious()) {
            log.error(e.getMessage(), e);
            return fail(HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
        if (printNonSeriousExceptionLog(e)) {
            log.warn(e.getMessage(), e);
        }
        return fail(HttpStatus.BAD_REQUEST, e);
    }

    @ExceptionHandler(Throwable.class)
    default R serviceErrorHandler(Throwable e) {
        log.error(e.getMessage(), e);
        return fail(HttpStatus.INTERNAL_SERVER_ERROR, e);
    }

    default R fail(HttpStatus httpStatus, Throwable e) {
        HyggeCode hyggeCode = null;
        if (e instanceof HyggeRuntimeException) {
            hyggeCode = ((HyggeRuntimeException) e).getHyggeCode();
        } else if (e instanceof HyggeException) {
            hyggeCode = ((HyggeException) e).getHyggeCode();
        }

        HyggeControllerResponse<?, ?> hyggeControllerResponse = new HyggeControllerResponse<>();

        ResponseEntity.BodyBuilder builder = getBuilder(httpStatus);

        if (hyggeCode != null) {
            hyggeControllerResponse.setCode(hyggeCode.getCode());
            hyggeControllerResponse.setMsg(hyggeCode.getPublicMessage() == null ? e.getMessage() : hyggeCode.getPublicMessage());
        } else {
            hyggeControllerResponse.setCode(GlobalHyggeCodeEnum.SERVER_END_EXCEPTION.getCode());
            hyggeControllerResponse.setMsg(GlobalHyggeCodeEnum.SERVER_END_EXCEPTION.getPublicMessage());
        }
        return (R) builder.body(hyggeControllerResponse);
    }

    default <T> R failWithWrapper(HttpStatus httpStatus, HttpHeaders headers, HyggeCode hyggeCode, String msg, T entity, Throwable e, HyggeControllerResponseWrapper<R> responseWrapper) {
        return responseWrapper.createResponse(httpStatus, headers, hyggeCode, msg, entity, e);
    }

    default R success() {
        return success(HttpStatus.OK, null, GlobalHyggeCodeEnum.SUCCESS, null, null);
    }

    default R successWithWrapper(HyggeControllerResponseWrapper<R> responseWrapper) {
        return successWithWrapper(HttpStatus.OK, null, GlobalHyggeCodeEnum.SUCCESS, null, null, responseWrapper);
    }

    default <T> R success(T entity) {
        return success(HttpStatus.OK, null, GlobalHyggeCodeEnum.SUCCESS, null, entity);
    }

    default <T> R successWithWrapper(T entity, HyggeControllerResponseWrapper<R> responseWrapper) {
        return successWithWrapper(HttpStatus.OK, null, GlobalHyggeCodeEnum.SUCCESS, null, entity, responseWrapper);
    }

    default <T> R success(HyggeCode hyggeCode, String msg, T entity) {
        return success(HttpStatus.OK, null, hyggeCode, msg, entity);
    }

    default <T> R successWithWrapper(HyggeCode hyggeCode, String msg, T entity, HyggeControllerResponseWrapper<R> responseWrapper) {
        return successWithWrapper(HttpStatus.OK, null, hyggeCode, msg, entity, responseWrapper);
    }

    default <T> R success(HttpStatus httpStatus, HttpHeaders headers, HyggeCode hyggeCode, String msg, T entity) {
        ResponseEntity.BodyBuilder builder = getBuilder(httpStatus);
        HyggeControllerResponse<?, T> hyggeControllerResponse = new HyggeControllerResponse<>();
        hyggeControllerResponse.setCode(hyggeCode.getCode());
        hyggeControllerResponse.setMsg(msg != null ? msg : hyggeCode.getPublicMessage());
        hyggeControllerResponse.setMain(entity);

        R response;
        if (headers != null) {
            response = (R) builder.headers(headers).body(hyggeControllerResponse);
        } else {
            response = (R) builder.body(hyggeControllerResponse);
        }
        return response;
    }

    default <T> R successWithWrapper(HttpStatus httpStatus, HttpHeaders headers, HyggeCode hyggeCode, String msg, T entity, HyggeControllerResponseWrapper<R> responseWrapper) {
        return responseWrapper.createResponse(httpStatus, headers, hyggeCode, msg, entity, null);
    }

    default ResponseEntity.BodyBuilder getBuilder(HttpStatus httpStatus) {
        // "status(HttpStatus status)" 方法在 Spring Boot 3.x 环境中不存在了，此处用 int 型重载进行兼容性优化
        ResponseEntity.BodyBuilder result = ResponseEntity.status(httpStatus.value());
        result.contentType(MediaType.APPLICATION_JSON);
        return result;
    }

    @FunctionalInterface
    interface HyggeControllerResponseWrapper<R extends ResponseEntity<?>> {
        /**
         * HyggeControllerResponse 包装器
         *
         * @return HyggeControllerResponse 的实例
         */
        R createResponse(HttpStatus httpStatus, HttpHeaders headers, HyggeCode hyggeCode, String msg, Object entity, Throwable throwable);
    }
}
