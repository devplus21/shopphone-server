package com.example.itshop.dto.client.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserLoginClientReqDto {
    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
