package com.example.itshop.dto.admin.response;

import com.example.itshop.entities.ProductAttribute;
import com.example.itshop.enums.ProductAttributeCode;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class ProductAttributeAdminResDto {
	private Long id;
	private ProductAttributeCode code;
	private String value;
	private OffsetDateTime createdAt;
	private OffsetDateTime updatedAt;
	
	public ProductAttributeAdminResDto(ProductAttribute productAttribute) {
		this.id = productAttribute.getId();
		this.code = productAttribute.getCode();
		this.value = productAttribute.getValue();
		this.createdAt = productAttribute.getCreatedAt();
		this.updatedAt = productAttribute.getUpdatedAt();
	}
}
