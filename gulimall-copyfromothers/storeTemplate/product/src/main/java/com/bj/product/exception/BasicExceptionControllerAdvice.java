package com.bj.product.exception;

import com.bj.exceptionCode.BASICTRANCODE;
import com.bj.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName BasicExceptionControllerAdvice
 * @Description TODO
 * @Author 13011
 * @Date 2020/8/29 22:39
 * @Version 1.0
 **/
@Slf4j
@RestControllerAdvice
public class BasicExceptionControllerAdvice {

    @ExceptionHandler
    public R handleExcept(MethodArgumentNotValidException e){
        BindingResult result = e.getBindingResult();
        Map<String,Object> errorResult=new HashMap();
        result.getFieldErrors().stream().forEach(fieldError -> {
            errorResult.put(fieldError.getField(),fieldError.getDefaultMessage());
        });
        log.info(e.getMessage());
        return R.error(BASICTRANCODE.VALIDEXCEPTION.getCode(),BASICTRANCODE.VALIDEXCEPTION.getMsg()).put("data",errorResult);
    }
    @ExceptionHandler
    public R handleValidExcept(Throwable throwable){
        log.info(throwable.getMessage(),throwable);
        return R.error(BASICTRANCODE.UNKONWEXCEPTION.getCode(),BASICTRANCODE.UNKONWEXCEPTION.getMsg());
    }

}
