package com.example.itshop.dto.admin.request;

import com.example.itshop.dto.common.PaginationRequestDto;
import com.example.itshop.enums.VoucherStatus;
import com.example.itshop.enums.VoucherType;
import lombok.Data;

@Data
public class VoucherSearchAdminReqDto extends PaginationRequestDto {
	private String searchText;
	private String startsAt;
	private String expiresAt;
	private VoucherStatus status;
	private VoucherType type;
}
