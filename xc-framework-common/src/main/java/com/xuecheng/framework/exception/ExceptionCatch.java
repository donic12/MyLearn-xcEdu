package com.xuecheng.framework.exception;


import com.google.common.collect.ImmutableMap;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Optional;

@RestControllerAdvice
public class ExceptionCatch {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionCatch.class);

    //定义map，配置异常类型所对应的错误代码,ImmutableMap线程安全，并且不可更改
    private static ImmutableMap<Class<? extends Throwable>, ResultCode> EXCEPTIONS;
    //定义map的builder对象，去构建ImmutableMap
    protected static ImmutableMap.Builder<Class<? extends Throwable>, ResultCode> builder = ImmutableMap.builder();

    static {
        builder.put(HttpMessageNotReadableException.class, CommonCode.INVALID_PARAM);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseResult customException(CustomException customException) {
        LOGGER.error("catch exception:{}", customException.getMessage());
        ResultCode resultCode = customException.getResultCode();
        return new ResponseResult(resultCode);
    }

//    @ExceptionHandler(Exception.class)
//    public ResponseResult exception(Exception exception) {
//        LOGGER.error("catch exception:{}", exception.getMessage());
//        if (EXCEPTIONS == null) {
//            EXCEPTIONS = builder.build();
//        }
//        ResultCode resultCode = EXCEPTIONS.get(exception.getClass());
//        if (resultCode != null) {
//            return new ResponseResult(resultCode);
//        } else {
//            return new ResponseResult(CommonCode.SERVER_ERROR);
//        }
//    }

    @ExceptionHandler(Exception.class)
    public ResponseResult exception1(Exception exception) {
        LOGGER.error("catch exception:{}", exception.getMessage());
        ResultCode resultCode = Optional
                .ofNullable(EXCEPTIONS)
                .orElse(builder.build())
                .get(exception.getClass());
        return Optional.ofNullable(resultCode).map(ResponseResult::new).orElse(new ResponseResult(CommonCode.SERVER_ERROR));
    }


}
