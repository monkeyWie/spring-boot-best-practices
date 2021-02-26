package com.github.monkeywie.springboot.example.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.monkeywie.springboot.example.feign.UserFeign;
import com.github.monkeywie.springboot.example.vo.Result;
import feign.*;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.spring.SpringContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

/**
 * @Author LiWei
 * @Description
 * @Date 2021/1/27 10:52
 */
@Configuration
public class FeignConfig {

    @Autowired
    private Environment environment;
    @Autowired
    private ObjectMapper objectMapper;

    // 构造user服务的feign客户端
    @Bean
    public UserFeign userFeign() {
        // 这里是因为方便演示，直接调用自身服务的接口
        String userServer = "http://127.0.0.1:" + environment.getProperty("server.port");
        return Feign.builder()
                .client(new FeignClient(null, null))
                .encoder(new GsonEncoder())
                .decoder(new GsonDecoder())
                .contract(new SpringContract()) // 这里很关键，要使用SpringMVC注解必须配置这个contract
                .retryer(Retryer.NEVER_RETRY)
                .requestInterceptor(template -> {
                    template.header("Content-Type", "application/json");
                })
                .options(new Request.Options(10, TimeUnit.SECONDS, 60, TimeUnit.SECONDS, true))
                .target(UserFeign.class, userServer);
    }

    public class FeignClient extends Client.Default {

        public FeignClient(final SSLSocketFactory sslContextFactory, final HostnameVerifier hostnameVerifier) {
            super(sslContextFactory, hostnameVerifier);
        }

        // 重写execute方法，在接口请求失败时抛出异常
        @Override
        public Response execute(final Request request, final Request.Options options) throws IOException {
            Response response = super.execute(request, options);
            if (response.status() == 200) {
                String body = Util.toString(response.body().asReader(Charset.forName("UTF-8")));
                Result result = null;
                try {
                    result = objectMapper.readValue(body, Result.class);
                } catch (Exception e) {

                }
                if (result == null || result.getCode() != 200) {
                    throw new FeignException.FeignServerException(200, "http request fail", request, body.getBytes());
                }
                // 注意这里因为把响应流读完了，所以要重新把body赋值，不然后续后报错
                response = response.toBuilder()
                        .body(body.getBytes())
                        .build();
            }
            return response;
        }
    }
}
