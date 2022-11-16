package com.example.itshop.dto.common;

import lombok.Data;

@Data
public class TokenResponseDto {
	public String accessToken;
	public String refreshToken;
}
