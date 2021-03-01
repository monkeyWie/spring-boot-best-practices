package com.github.monkeywie.springboot.example.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author LiWei
 * @Description
 * @Date 2021/1/25 17:08
 */
@Configuration
public class WebConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebConfig.class);


    @Bean
    public OncePerRequestFilter contentCachingRequestFilter() {
        return new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain) throws ServletException, IOException {
                ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
                ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);
                filterChain.doFilter(wrappedRequest, wrappedResponse);
                LOGGER.info("http request:{}", new String(wrappedRequest.getContentAsByteArray()));
                LOGGER.info("http response:{}", new String(wrappedResponse.getContentAsByteArray()));
                wrappedResponse.copyBodyToResponse();
            }
        };
    }
}
