package com.example.itshop.dto.admin.request;

import com.example.itshop.dto.common.PaginationRequestDto;
import lombok.Data;

@Data
public class AccountSearchAdminReqDto extends PaginationRequestDto {
	private String searchText;
}
