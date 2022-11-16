package com.example.itshop.dto.admin.request;

import com.example.itshop.dto.common.PaginationRequestDto;
import com.example.itshop.enums.ProductStatus;
import lombok.Data;

@Data
public class ProductSearchAdminReqDto extends PaginationRequestDto {
	private String searchText;
	private Long brandId;
	private ProductStatus status;
}
