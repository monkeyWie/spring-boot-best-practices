package com.github.monkeywie.springboot.example.dto;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Author LiWei
 * @Description
 * @Date 2021/2/26 14:28
 */
@Data
public class UserAddDTO {
    @NotBlank
    private String name;
    @NotNull
    @Min(0)
    @Max(150)
    private Integer age;
}
