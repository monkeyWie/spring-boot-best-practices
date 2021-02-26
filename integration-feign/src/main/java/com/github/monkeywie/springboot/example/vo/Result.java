package com.github.monkeywie.springboot.example.vo;

import lombok.Builder;
import lombok.Data;

/**
 * @Author LiWei
 * @Description 通用响应体
 * @Date 2021/2/26 14:24
 */
@Data
@Builder
public class Result<T> {

    public static final int CODE_OK = 200;
    public static final int CODE_PARAMS_INVALID = 400;
    public static final int CODE_SERVER_ERROR = 500;

    private int code;
    private T data;
    private String msg;

    public static <T> Result<T> ok(T data) {
        return Result.<T>builder()
                .code(CODE_OK)
                .data(data)
                .build();
    }

    public static <T> Result<T> ok() {
        return Result.ok(null);
    }

    public static <T> Result<T> fail(int code, String msg) {
        return Result.<T>builder()
                .code(code)
                .msg(msg)
                .build();
    }
}
