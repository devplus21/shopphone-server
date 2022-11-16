package com.example.itshop.dto.admin.response;

import com.example.itshop.dto.common.TokenResponseDto;
import com.example.itshop.entities.Admin;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class LoginAdminResDto extends TokenResponseDto {
	private Long id;
	private String email;
	private String name;
	private OffsetDateTime createdAt;
	private OffsetDateTime updatedAt;
	
	public LoginAdminResDto(Admin admin, String accessToken, String refreshToken) {
		this.id = admin.getId();
		this.email = admin.getEmail();
		this.name = admin.getName();
		this.createdAt = admin.getCreatedAt();
		this.updatedAt = admin.getUpdatedAt();
		this.accessToken = accessToken;
	}
}
