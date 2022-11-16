package com.example.itshop.annotations;

import com.example.itshop.constants.AppConstant;
import com.example.itshop.enums.ClientType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@SecurityRequirement(name = AppConstant.SWAGGER_BEARER_KEY)
public @interface AppAuth {
    ClientType[] clientTypes();
}
