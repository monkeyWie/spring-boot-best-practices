package com.github.monkeywie.springboot.example.feign;

import com.github.monkeywie.springboot.example.dto.UserAddDTO;
import com.github.monkeywie.springboot.example.vo.Result;
import com.github.monkeywie.springboot.example.vo.UserVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author LiWei
 * @Description 声明user服务接口列表
 * @Date 2021/2/26 17:18
 */
public interface UserFeign {
    @PostMapping("/users")
    Result<Void> add(@RequestBody UserAddDTO dto);

    @GetMapping("/users")
    Result<UserVO> find(@RequestParam("name") String name);

    @GetMapping("/test/404")
    Result<UserVO> error404();
}
