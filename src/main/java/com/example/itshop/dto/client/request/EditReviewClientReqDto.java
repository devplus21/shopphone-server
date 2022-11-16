package com.example.itshop.dto.client.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class EditReviewClientReqDto {
	@NotNull
	private Long id;
	@NotBlank
	private Integer rating;
	@NotBlank
	private String comment;
}
