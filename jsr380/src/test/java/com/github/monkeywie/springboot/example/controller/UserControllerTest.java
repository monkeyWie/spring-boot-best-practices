package com.github.monkeywie.springboot.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.monkeywie.springboot.example.dto.UserAddDTO;
import com.github.monkeywie.springboot.example.vo.Result;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.MockMvcPrint;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.net.URI;
import java.util.Locale;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @Author LiWei
 * @Description
 * @Date 2021/2/26 14:37
 */
@SpringBootTest
@AutoConfigureMockMvc(print = MockMvcPrint.NONE)
@ContextConfiguration
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void addNameBlankTest() throws Exception {
        UserAddDTO dto = new UserAddDTO();
        dto.setAge(18);
        commonMock(HttpMethod.POST, "/users", dto)
                .andExpect(jsonPath("$.code").value(Result.CODE_PARAMS_INVALID));
    }

    @Test
    public void addAgeNullTest() throws Exception {
        UserAddDTO dto = new UserAddDTO();
        dto.setName("java");
        commonMock(HttpMethod.POST, "/users", dto)
                .andExpect(jsonPath("$.code").value(Result.CODE_PARAMS_INVALID));
    }

    @Test
    public void addAgeMinTest() throws Exception {
        UserAddDTO dto = new UserAddDTO();
        dto.setName("java");
        dto.setAge(-1);
        commonMock(HttpMethod.POST, "/users", dto)
                .andExpect(jsonPath("$.code").value(Result.CODE_PARAMS_INVALID));
    }

    @Test
    public void addAgeMaxTest() throws Exception {
        UserAddDTO dto = new UserAddDTO();
        dto.setName("java");
        dto.setAge(200);
        commonMock(HttpMethod.POST, "/users", dto)
                .andExpect(jsonPath("$.code").value(Result.CODE_PARAMS_INVALID));
    }

    @Test
    public void addSuccess() throws Exception {
        UserAddDTO dto = new UserAddDTO();
        dto.setName("java");
        dto.setAge(18);
        commonMock(HttpMethod.POST, "/users", dto)
                .andExpect(jsonPath("$.code").value(Result.CODE_OK));
    }

    @Test
    public void findNameBlank() throws Exception {
        commonMock(HttpMethod.GET, "/users", null)
                .andExpect(jsonPath("$.code").value(Result.CODE_PARAMS_INVALID));

    }

    @Test
    public void findSuccess() throws Exception {
        commonMock(HttpMethod.GET, "/users?name=java", null)
                .andExpect(jsonPath("$.code").value(Result.CODE_OK))
                .andExpect(jsonPath("$.data").isNotEmpty());
    }

    private ResultActions commonMock(HttpMethod method, String uri, Object dto) throws Exception {
        MockHttpServletRequestBuilder requestBuilder = request(method, URI.create(uri))
                .locale(Locale.CHINA);
        if (dto != null) {
            requestBuilder.contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(dto));
        }
        ResultActions resultActions = this.mockMvc.perform(requestBuilder);
        // 修复响应日志中文乱码问题
        resultActions.andReturn()
                .getResponse()
                .setCharacterEncoding("UTF-8");
        return resultActions.andDo(print())
                .andExpect(status().is(200));

    }
}
