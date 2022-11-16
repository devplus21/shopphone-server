package com.example.itshop.configs;

import com.example.itshop.constants.AppConstant;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootConfiguration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {

        SecurityScheme scheme =  new SecurityScheme()
                .type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT");

        Components components = new Components()
                .addSecuritySchemes(AppConstant.SWAGGER_BEARER_KEY, scheme);

        return new OpenAPI().components(components);
    }
}
