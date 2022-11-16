package com.example.itshop.dto.client.response;

import com.example.itshop.entities.Brand;
import lombok.Data;

@Data
public class BrandClientResDto {
    private Long id;

    private String name;

    private String code;

    private String createdAt;

    private String updatedAt;

    public BrandClientResDto(Brand brand) {
        this.id = brand.getId();
        this.name = brand.getName();
        this.createdAt = brand.getCreatedAt().toString();
        this.updatedAt = brand.getUpdatedAt().toString();
        this.code = brand.getCode();
    }
}
