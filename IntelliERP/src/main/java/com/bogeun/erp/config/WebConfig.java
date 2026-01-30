package com.bogeun.erp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 모든 주소에 대해서
                .allowedOrigins("http://localhost:5173") // React 포트만 허용하겠다!
                .allowedMethods("GET", "POST", "PUT", "DELETE") // 이런 요청들을 허용한다
                .allowCredentials(true);
    }
}