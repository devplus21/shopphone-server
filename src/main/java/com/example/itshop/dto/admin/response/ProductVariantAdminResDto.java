package com.example.itshop.dto.admin.response;

import com.example.itshop.entities.ProductAttribute;
import com.example.itshop.entities.ProductVariant;
import com.example.itshop.entities.ProductVariantImage;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

@Data
public class ProductVariantAdminResDto {
	private Long id;
	private Double price;
	private Long stock;
	@JsonProperty(value = "attributes")
	private Collection<ProductAttributeAdminResDto> attributeAdminResDtos;
	
	@JsonProperty(value = "images")
	private Collection<ProductVariantImageAdminResDto> imageAdminResDtos;
	private OffsetDateTime createdAt;
	private OffsetDateTime updatedAt;
	
	public ProductVariantAdminResDto(ProductVariant productVariant) {
		this.id = productVariant.getId();
		this.price = productVariant.getPrice();
		this.stock = productVariant.getStock();
		this.createdAt = productVariant.getCreatedAt();
		this.updatedAt = productVariant.getUpdatedAt();
	}
	
	public ProductVariantAdminResDto(ProductVariant productVariant, Collection<ProductAttribute> productAttributes,
									 Collection<ProductVariantImage> productVariantImages) {
		this(productVariant);
		this.attributeAdminResDtos = productAttributes.stream()
			.map(ProductAttributeAdminResDto::new).collect(Collectors.toList());
		this.imageAdminResDtos = productVariantImages.stream()
			.map(ProductVariantImageAdminResDto::new).collect(Collectors.toList());
	}
}
