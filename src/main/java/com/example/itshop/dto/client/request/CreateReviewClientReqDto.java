package com.example.itshop.dto.client.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CreateReviewClientReqDto {
	@NotBlank
	private Integer rating;
	@NotBlank
	private String comment;
}
