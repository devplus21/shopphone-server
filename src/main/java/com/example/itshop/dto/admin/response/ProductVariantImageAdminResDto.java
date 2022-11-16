package com.example.itshop.dto.admin.response;

import com.example.itshop.dto.common.FileResponseDto;
import com.example.itshop.entities.File;
import com.example.itshop.entities.ProductVariantImage;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ProductVariantImageAdminResDto {
	private Long id;
	
	@JsonProperty("file")
	private FileResponseDto fileResponseDto;
	
	public ProductVariantImageAdminResDto(ProductVariantImage productVariantImage) {
		this.id = productVariantImage.getId();
		this.fileResponseDto = new FileResponseDto(productVariantImage.getFile());
	}
}
