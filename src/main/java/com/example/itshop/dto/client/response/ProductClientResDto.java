package com.example.itshop.dto.client.response;

import com.example.itshop.entities.Product;
import com.example.itshop.entities.Review;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class ProductClientResDto {
	
	private Long id;
	
	private String name;
	
	private Double rating;
	
	@JsonProperty(value = "brand")
	private BrandClientResDto brandClientResDto;
	
	@JsonProperty(value = "productVariants")
	private List<ProductVariantClientResDto> variantResponseDtos;
	
	public ProductClientResDto(Product product) {
		this.id = product.getId();
		this.name = product.getName();
		this.brandClientResDto = new BrandClientResDto(product.getBrand());
		this.variantResponseDtos = product.getProductVariants().stream().
			map(ProductVariantClientResDto::new).collect(Collectors.toList());
		this.rating = ((double) product.getReviews().stream().map(Review::getRating)
			.reduce(Integer::sum).orElse(0)) / product.getReviews().size();
	}
}
