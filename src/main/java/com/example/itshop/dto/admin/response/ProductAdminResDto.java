package com.example.itshop.dto.admin.response;

import com.example.itshop.entities.Brand;
import com.example.itshop.entities.File;
import com.example.itshop.entities.Product;
import com.example.itshop.entities.ProductVariant;
import com.example.itshop.enums.ProductStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

@Data
public class ProductAdminResDto {
	private Long id;
	private String name;
	private Brand brand;
	private ProductStatus status;
	private File thumbnail;
	@JsonProperty(value = "variants")
	private Collection<ProductVariantAdminResDto> variantAdminResDtos;
	private OffsetDateTime createdAt;
	private OffsetDateTime updatedAt;
	
	public ProductAdminResDto(Product product) {
		this.id = product.getId();
		this.name = product.getName();
		this.status = product.getStatus();
		this.createdAt = product.getCreatedAt();
		this.updatedAt = product.getUpdatedAt();
	}
	
	public ProductAdminResDto(Product product, Collection<ProductVariant> productVariants) {
		this(product);
		this.variantAdminResDtos = productVariants.stream()
			.map(productVariant -> new ProductVariantAdminResDto(productVariant,
				productVariant.getProductAttributes(), productVariant.getProductVariantImages())
			)
			.collect(Collectors.toList());
	}
}
