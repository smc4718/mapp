package com.pyj.mapp.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebMvc
@RequiredArgsConstructor
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 정적 리소스
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/", "classpath:/templates/");

        // 공지 이미지
        registry.addResourceHandler("/mapp/notice/**")
                .addResourceLocations("file:/mapp/notice/");

        // 파일 업로드
        registry.addResourceHandler("/mapp/upload/**")
                .addResourceLocations("file:/mapp/upload/");
    }
}