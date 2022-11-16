package com.example.itshop.dto.client.response;

import com.example.itshop.entities.UserVoucher;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class UserVoucherClientResDto {
	
	private Long id;
	private OffsetDateTime claimDate;
	
	@JsonProperty(value = "voucher")
	private VoucherClientResDto voucher;
	
	public UserVoucherClientResDto(UserVoucher userVoucher) {
		this.claimDate = userVoucher.getCreatedAt();
		this.id = userVoucher.getId();
		voucher = new VoucherClientResDto(userVoucher.getVoucher());
	}
}
