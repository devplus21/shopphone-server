package com.example.itshop.dto.admin.response;

import com.example.itshop.entities.Brand;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class BrandAdminResDto {
	private Long id;
	private String name;
	private String code;
	private OffsetDateTime createdAt;
	private OffsetDateTime updatedAt;
	
	public BrandAdminResDto(Brand brand) {
		this.id = brand.getId();
		this.name = brand.getName();
		this.code = brand.getCode();
		this.createdAt = brand.getCreatedAt();
		this.updatedAt = brand.getUpdatedAt();
	}
	
}
