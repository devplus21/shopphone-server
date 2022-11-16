package com.example.itshop.dto.client.request;

import com.example.itshop.dto.common.PaginationRequestDto;
import com.example.itshop.enums.VoucherType;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class VoucherSearchClientReqDto extends PaginationRequestDto {
	@NotNull
	private VoucherType voucherType;
}
