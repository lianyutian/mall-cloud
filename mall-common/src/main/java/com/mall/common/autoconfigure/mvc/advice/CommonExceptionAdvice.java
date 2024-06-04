package com.mall.common.autoconfigure.mvc.advice;


import com.mall.common.constants.Constant;
import com.mall.common.domain.Result;
import com.mall.common.exceptions.CommonException;
import com.mall.common.exceptions.DbException;
import com.mall.common.utils.WebUtils;
import jakarta.servlet.ServletException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 服务统一异常处理
 *
 * @author : [lm]
 * @version : [v1.0]
 * @createTime : [2024/4/1 21:46]
 */
@RestControllerAdvice
@Slf4j
public class CommonExceptionAdvice {
    @ExceptionHandler(DbException.class)
    public Object handleDbException(DbException e) {
        log.error("数据库操作异常 -> {}", e.getMessage(), e);
        return processResponse(e.getStatus(), e.getCode(), e.getMessage());
    }

    @ExceptionHandler(CommonException.class)
    public Object handleBadRequestException(CommonException e) {
        log.error("自定义异常 -> {} , 状态码：{}, 异常原因：{}  ", e.getClass().getName(), e.getStatus(), e.getMessage());
        return processResponse(e.getStatus(), e.getCode(), e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Object handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String msg = e.getBindingResult().getAllErrors()
                .stream().map(ObjectError::getDefaultMessage)
                .collect(Collectors.joining("|"));
        log.error("请求参数校验异常 -> {}", msg, e);
        return processResponse(400, 400, msg);
    }

    @ExceptionHandler(BindException.class)
    public Object handleBindException(BindException e) {
        log.error("请求参数绑定异常 ->BindException, {}", e.getMessage(), e);
        return processResponse(400, 400, "请求参数格式错误");
    }

    @ExceptionHandler(ServletException.class)
    public Object handleNestedServletException(ServletException e) {
        log.error("参数异常 -> ServletException, {}", e.getMessage(), e);
        return processResponse(400, 400, "请求参数异常");
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public Object handViolationException(ConstraintViolationException e) {
        log.error("请求参数异常 -> ConstraintViolationException, {}", e.getMessage(), e);
        return processResponse(HttpStatus.OK.value(), HttpStatus.BAD_REQUEST.value(),
                e.getConstraintViolations().stream()
                        .map(ConstraintViolation::getMessage)
                        .distinct()
                        .collect(Collectors.joining("|"))
        );
    }

    @ExceptionHandler(Exception.class)
    public Object handleRuntimeException(Exception e) {
        log.error("其他异常 uri : {} -> {}",
                Objects.requireNonNull(WebUtils.getRequest()).getRequestURI(), e.getMessage(), e);
        return processResponse(500, 500, "服务器内部异常");
    }

    private Object processResponse(int status, int code, String msg) {
        // 1.标记响应异常已处理（避免重复处理）
        WebUtils.setResponseHeader(Constant.BODY_PROCESSED_MARK_HEADER, "true");
        // 2.如果是网关请求，http状态码修改为200返回，前端基于业务状态码code来判断状态
        // 如果是微服务请求，http状态码基于异常原样返回，微服务自己做fallback处理
        return WebUtils.isGatewayRequest() ?
                Result.error(code, msg).requestId(MDC.get(Constant.REQUEST_ID_HEADER))
                : ResponseEntity.status(status).body(msg);
    }
}
