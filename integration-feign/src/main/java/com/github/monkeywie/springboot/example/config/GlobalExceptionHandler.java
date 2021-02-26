package com.github.monkeywie.springboot.example.config;

import com.github.monkeywie.springboot.example.vo.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @Author LiWei
 * @Description 全局异常处理
 * @Date 2020/4/27 13:39
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<Result> exceptionHandler(HttpServletRequest request, Exception e) {
        LOGGER.error(request.getRequestURI(), e);
        return ResponseEntity.ok(Result.fail(Result.CODE_SERVER_ERROR, "server error"));
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class, BindException.class})
    public ResponseEntity<Result> methodArgumentNotValidHandler(HttpServletRequest request, Exception e) {
        BindingResult bindingResult;
        if (e instanceof MethodArgumentNotValidException) {
            //@RequestBody参数校验
            bindingResult = ((MethodArgumentNotValidException) e).getBindingResult();
        } else {
            //@ModelAttribute参数校验
            bindingResult = ((BindException) e).getBindingResult();
        }
        FieldError fieldError = bindingResult.getFieldError();
        return ResponseEntity.ok(Result.fail(Result.CODE_PARAMS_INVALID, "[" + fieldError.getField() + "]" + fieldError.getDefaultMessage()));
    }

    //@RequestParam参数校验
    @ExceptionHandler(value = {ConstraintViolationException.class, MissingServletRequestParameterException.class})
    public ResponseEntity<Result> constraintViolationHandler(Exception e) {
        String field;
        String msg;
        if (e instanceof ConstraintViolationException) {
            ConstraintViolation<?> constraintViolation = ((ConstraintViolationException) e).getConstraintViolations().stream().findFirst().get();
            List<Path.Node> pathList = StreamSupport.stream(constraintViolation.getPropertyPath().spliterator(), false)
                    .collect(Collectors.toList());
            field = pathList.get(pathList.size() - 1).getName();
            msg = constraintViolation.getMessage();
        } else {
            // 这个不是JSR标准返回的异常，要自定义提示文本
            field = ((MissingServletRequestParameterException) e).getParameterName();
            msg = "不能为空";
        }
        return ResponseEntity.ok(Result.fail(Result.CODE_PARAMS_INVALID, "[" + field + "]" + msg));
    }

}
