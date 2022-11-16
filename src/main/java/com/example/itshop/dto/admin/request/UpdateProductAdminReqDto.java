package com.example.itshop.dto.admin.request;

import com.example.itshop.enums.ProductStatus;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UpdateProductAdminReqDto {
	@NotNull
	private Long productId;
	
	private String name;
	
	private String description;
	
	private Long brandId;
	
	private ProductStatus status;
	
	private Long thumbnailId;
}
