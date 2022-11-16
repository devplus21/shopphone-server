package com.example.itshop.dto.client.response;

import com.example.itshop.entities.Cart;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CartClientResDto {
	private Long id;
	private Long quantity;
	
	@JsonProperty(value = "productVariants")
	private ProductVariantClientResDto productVariantClientResDto;
	
	public CartClientResDto(Cart cart) {
		this.id = cart.getId();
		this.quantity = cart.getQuantity();
		this.productVariantClientResDto = new ProductVariantClientResDto(cart.getProductVariant());
	}
}
