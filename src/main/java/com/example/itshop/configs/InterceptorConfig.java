package com.example.itshop.configs;

import com.example.itshop.intercepters.SecurityInterceptor;
import com.example.itshop.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootConfiguration
@RequiredArgsConstructor
public class InterceptorConfig implements WebMvcConfigurer {

    private final SecurityUtil securityUtil;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SecurityInterceptor(securityUtil));
    }
}
