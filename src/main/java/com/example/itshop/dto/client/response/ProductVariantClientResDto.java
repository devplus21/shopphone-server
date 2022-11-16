package com.example.itshop.dto.client.response;

import com.example.itshop.dto.common.FileResponseDto;
import com.example.itshop.entities.ProductAttribute;
import com.example.itshop.entities.ProductVariant;
import com.example.itshop.entities.ProductVariantImage;
import com.example.itshop.enums.ProductAttributeCode;
import lombok.Data;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class ProductVariantClientResDto {
	private Long id;
	private Double price;
	private Long stock;
	
	Map<ProductAttributeCode, String> attributes;
	
	Collection<FileResponseDto> images;
	
	public ProductVariantClientResDto(ProductVariant productVariant) {
		this.id = productVariant.getId();
		this.price = productVariant.getPrice();
		this.stock = productVariant.getStock();
		this.attributes = productVariant.getProductAttributes()
			.stream().collect(Collectors.toMap(
				ProductAttribute::getCode,
				ProductAttribute::getValue
			));
		this.images = productVariant.getProductVariantImages().stream()
			.map(ProductVariantImage::getFile)
			.map(FileResponseDto::new).collect(Collectors.toList());
		System.out.println("123");
	}
}
