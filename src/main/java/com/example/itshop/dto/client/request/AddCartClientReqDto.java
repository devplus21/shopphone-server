package com.example.itshop.dto.client.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AddCartClientReqDto {
	@NotNull
	private Long productVariantId;
	
	@NotNull
	private Long quantity;
}
