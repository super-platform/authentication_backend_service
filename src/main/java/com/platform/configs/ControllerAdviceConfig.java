package com.platform.configs;

import com.platform.common.constant.ResponseCode;
import com.platform.common.dto.ResponseWithBody;
import com.platform.common.utils.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import java.util.HashSet;
import java.util.Set;


@ControllerAdvice
@Slf4j
public class ControllerAdviceConfig {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<ResponseWithBody<?>> handleNullPointerException(MethodArgumentNotValidException e, WebRequest request){
        log.error("URI: {}; NullPointerException: {}", request.getContextPath(), e.getStackTrace());

        Set<String> errorFields = new HashSet<>();
        e.getBindingResult().getFieldErrors().forEach((fieldError -> {
            String fieldName = fieldError.getField();
            errorFields.add(fieldName);
        }));

        return new ResponseEntity<>(new ResponseWithBody<>(null, ResponseUtil.createFailedInvalidInputStatus(ResponseCode.E999, errorFields)), HttpStatus.OK);
    }
}
