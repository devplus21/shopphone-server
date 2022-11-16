package com.example.itshop.dto.client.response;

import com.example.itshop.dto.common.FileResponseDto;
import com.example.itshop.dto.common.TokenResponseDto;
import com.example.itshop.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.Objects;

@Data
@AllArgsConstructor
public class LoginClientResDto extends TokenResponseDto {
	private OffsetDateTime createdAt;
	private OffsetDateTime updatedAt;
	private Long id;
	private String email;
	private String name;
	private FileResponseDto avatar;
	
	public LoginClientResDto(User user, String accessToken, String refreshToken) {
		this.id = user.getId();
		this.email = user.getEmail();
		this.name = user.getName();
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		if (Objects.nonNull(user.getAvatar())) {
			this.avatar = new FileResponseDto(user.getAvatar());
		}
	}
}
