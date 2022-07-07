package hygge.web.template;

import hygge.commons.exceptions.ExternalException;
import hygge.commons.exceptions.ExternalRuntimeException;
import hygge.commons.exceptions.InternalException;
import hygge.commons.exceptions.InternalRuntimeException;
import hygge.commons.exceptions.LightException;
import hygge.commons.exceptions.LightRuntimeException;
import hygge.commons.exceptions.ParameterException;
import hygge.commons.exceptions.ParameterRuntimeException;
import hygge.commons.exceptions.UtilException;
import hygge.commons.exceptions.UtilRuntimeException;
import hygge.commons.exceptions.code.GlobalHyggeCode;
import hygge.commons.exceptions.code.HyggeCode;
import hygge.commons.exceptions.core.HyggeException;
import hygge.commons.exceptions.core.HyggeRuntimeException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Hygge Controller 接口
 *
 * @author Xavier
 * @date 2022/7/5
 * @since 1.0
 */
@ControllerAdvice
public interface HyggeController<R extends ResponseEntity<?>> {
    Logger log = LogManager.getLogger(HyggeController.class);

    @ExceptionHandler({
            ExternalException.class,
            InternalException.class,
            UtilException.class,
            LightException.class,
            ParameterException.class
    })
    default R hyggeExceptionHandler(HyggeException e) {
        if (e.getHyggeCode().serious()) {
            log.error(e::getMessage, e);
            return fail(HttpStatus.INTERNAL_SERVER_ERROR, e);
        } else {
            log.warn(e::getMessage, e);
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
        if (e.getHyggeCode().serious()) {
            log.error(e::getMessage, e);
            return fail(HttpStatus.INTERNAL_SERVER_ERROR, e);
        } else {
            log.warn(e::getMessage, e);
            return fail(HttpStatus.BAD_REQUEST, e);
        }
    }

    @ExceptionHandler(Throwable.class)
    default R serviceErrorHandler(Throwable e) {
        log.error(e::getMessage, e);
        return fail(HttpStatus.INTERNAL_SERVER_ERROR, e);
    }

    default R fail(HttpStatus httpStatus, Throwable e) {
        HyggeCode<?, ?> hyggeCode = null;
        if (e instanceof HyggeRuntimeException) {
            hyggeCode = ((HyggeRuntimeException) e).getHyggeCode();
        } else if (e instanceof HyggeException) {
            hyggeCode = ((HyggeException) e).getHyggeCode();
        }

        HyggeResponse<?, ?> hyggeResponse = new HyggeResponse<>();

        ResponseEntity.BodyBuilder builder = getBuilder(httpStatus);

        if (hyggeCode != null) {
            hyggeResponse.setCode(hyggeCode.getCode());
            hyggeResponse.setMsg(hyggeCode.getPublicMessage() == null ? e.getMessage() : hyggeCode.getPublicMessage());
        } else {
            hyggeResponse.setCode(GlobalHyggeCode.SERVER_END_EXCEPTION.getCode());
            hyggeResponse.setMsg(GlobalHyggeCode.SERVER_END_EXCEPTION.getPublicMessage());
        }
        return (R) builder.body(hyggeResponse);
    }

    default <T> R fail(HttpStatus httpStatus, HttpHeaders headers, HyggeCode<?, ?> hyggeCode, String msg, T entity, Throwable e, HyggeResponseWrapper<R> responseWrapper) {
        return responseWrapper.createFailResponseData(httpStatus, headers, hyggeCode, msg, entity, e);
    }

    default R success() {
        return success(HttpStatus.OK, null, GlobalHyggeCode.SUCCESS, null, null);
    }

    default <T> R success(T entity) {
        return success(HttpStatus.OK, null, GlobalHyggeCode.SUCCESS, null, entity);
    }

    default <T> R success(HyggeCode<?, ?> hyggeCode, String msg, T entity) {
        return success(HttpStatus.OK, null, hyggeCode, msg, entity);
    }

    default <T> R success(HttpStatus httpStatus, HttpHeaders headers, HyggeCode<?, ?> hyggeCode, String msg, T entity) {
        ResponseEntity.BodyBuilder builder = getBuilder(httpStatus);
        HyggeResponse<?, T> hyggeResponse = new HyggeResponse<>();
        hyggeResponse.setCode(hyggeCode.getCode());
        hyggeResponse.setMsg(msg != null ? msg : hyggeCode.getPublicMessage());
        hyggeResponse.setMain(entity);

        R response;
        if (headers != null) {
            response = (R) builder.headers(headers).body(hyggeResponse);
        } else {
            response = (R) builder.body(hyggeResponse);
        }
        return response;
    }

    default <T> R success(HttpStatus httpStatus, HttpHeaders headers, HyggeCode<?, ?> hyggeCode, String msg, T entity, HyggeResponseWrapper<R> responseWrapper) {
        return responseWrapper.createFailResponseData(httpStatus, headers, hyggeCode, msg, entity, null);
    }

    default ResponseEntity.BodyBuilder getBuilder(HttpStatus httpStatus) {
        ResponseEntity.BodyBuilder result = ResponseEntity.status(httpStatus);
        result.contentType(MediaType.APPLICATION_JSON);
        return result;
    }

    @FunctionalInterface
    interface HyggeResponseWrapper<R> {
        /**
         * HyggeControllerResponse 包装器
         *
         * @return HyggeControllerResponse 的实例
         */
        <T> R createFailResponseData(HttpStatus httpStatus, HttpHeaders headers, HyggeCode<?, ?> hyggeCode, String msg, T entity, Throwable throwable);
    }
}