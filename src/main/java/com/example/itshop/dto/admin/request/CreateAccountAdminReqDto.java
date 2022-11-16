package com.example.itshop.dto.admin.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CreateAccountAdminReqDto {
	@NotBlank
	private String email;
	@NotBlank
	private String password;
}
