package com.github.monkeywie.springboot.example.feign;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.monkeywie.springboot.example.Application;
import com.github.monkeywie.springboot.example.dto.UserAddDTO;
import com.github.monkeywie.springboot.example.vo.Result;
import com.github.monkeywie.springboot.example.vo.UserVO;
import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


/**
 * @Author LiWei
 * @Description
 * @Date 2021/2/26 17:30
 */
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ContextConfiguration
public class UserFeignTest {
    @Autowired
    private UserFeign userFeign;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void addOk() throws Exception {
        UserAddDTO dto = new UserAddDTO();
        dto.setName("java");
        dto.setAge(18);
        Result<Void> result = userFeign.add(dto);
        assertEquals(result.getCode(), Result.CODE_OK);
        System.out.println(objectMapper.writeValueAsString(result));
    }

    @Test
    public void addFail() throws Exception {
        UserAddDTO dto = new UserAddDTO();
        dto.setAge(18);
        FeignException e = assertThrows(FeignException.class, () -> userFeign.add(dto));
        System.out.println(e.contentUTF8());
    }

    @Test
    public void queryOk() throws Exception {
        Result<UserVO> result = userFeign.find("java");
        assertEquals(result.getCode(), Result.CODE_OK);
        System.out.println(objectMapper.writeValueAsString(result));
    }

    @Test
    public void queryFail() throws Exception {
        FeignException e = assertThrows(FeignException.class, () -> userFeign.find(""));
        System.out.println(e.contentUTF8());
    }

    @Test
    public void test404() throws Exception {
        FeignException e = assertThrows(FeignException.class, () -> userFeign.error404());
        System.out.println(e.contentUTF8());
    }
}