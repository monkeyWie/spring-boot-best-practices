package com.github.monkeywie.springboot.example.controller;

import com.github.monkeywie.springboot.example.dto.UserAddDTO;
import com.github.monkeywie.springboot.example.vo.Result;
import com.github.monkeywie.springboot.example.vo.UserVO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;

/**
 * @Author LiWei
 * @Description
 * @Date 2021/2/26 14:24
 */
@RestController
@RequestMapping("users")
@Validated
public class UserController {

    @PostMapping
    public Result<Void> add(@Validated @RequestBody UserAddDTO dto) {
        return Result.ok();
    }

    @GetMapping
    public Result<UserVO> find(@NotBlank @RequestParam String name) {
        UserVO vo = new UserVO();
        vo.setId(1L);
        vo.setName("java");
        vo.setAge(18);
        return Result.ok(vo);
    }
}

