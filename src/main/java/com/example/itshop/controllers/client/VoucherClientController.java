package com.example.itshop.controllers.client;

import com.example.itshop.annotations.AppAuth;
import com.example.itshop.dto.common.PaginationRequestDto;
import com.example.itshop.dto.client.request.VoucherSearchClientReqDto;
import com.example.itshop.dto.common.PaginationResponseDto;
import com.example.itshop.dto.client.response.UserVoucherClientResDto;
import com.example.itshop.dto.client.response.VoucherClientResDto;
import com.example.itshop.enums.ClientType;
import com.example.itshop.services.client.VoucherClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@AppAuth(clientTypes = ClientType.USER)
@RequestMapping("${api.path.client}/voucher")
public class VoucherClientController {
	private final VoucherClientService voucherClientService;
	
	@GetMapping("all")
	public PaginationResponseDto<VoucherClientResDto> getAll(VoucherSearchClientReqDto dto) {
		return voucherClientService.getAll(dto);
	}
	
	@GetMapping("me")
	public PaginationResponseDto<UserVoucherClientResDto> getMyVoucher(PaginationRequestDto dto) {
		return voucherClientService.getMyVoucher(dto);
	}
	
	@PostMapping("claim/{voucherId}")    	
	public UserVoucherClientResDto claimVoucher(@PathVariable Long voucherId) {
		return voucherClientService.claimVoucher(voucherId);
	}
}
