package com.example.itshop.dto.client.response;

import com.example.itshop.dto.admin.response.VoucherConstraintUserAdminResDto;
import com.example.itshop.entities.Voucher;
import com.example.itshop.enums.VoucherStatus;
import com.example.itshop.enums.VoucherType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class VoucherClientResDto {
	private Long id;
	private String name;
	private Double discountAmount;
	private String unit;
	private OffsetDateTime startsAt;
	private OffsetDateTime expiresAt;
	private OffsetDateTime createdAt;
	private OffsetDateTime updatedAt;
	private VoucherType type;
	private Long quantity;
	private Long usedAmount;
	private Boolean isUsed;
	private VoucherStatus status;
	@JsonProperty(value = "voucherConstraintsUser")
	private VoucherConstraintUserClientResDto constraintUserResponseDto;
	@JsonProperty(value = "voucherConstraintsProduct")
	private VoucherConstraintProductClientResDto constraintProductResponseDto;
	
	public VoucherClientResDto(Voucher voucher) {
		this.id = voucher.getId();
		this.name = voucher.getName();
		this.discountAmount = voucher.getDiscountAmount();
		this.unit = voucher.getUnit().name();
		this.startsAt = voucher.getStartsAt();
		this.expiresAt = voucher.getExpiresAt();
		this.createdAt = voucher.getCreatedAt();
		this.updatedAt = voucher.getUpdatedAt();
		this.type = voucher.getType();
		this.quantity = voucher.getQuantity();
		this.usedAmount = voucher.getUsedAmount();
		this.status = voucher.getStatus();
		this.constraintUserResponseDto = new VoucherConstraintUserClientResDto(voucher.getVoucherConstraintUser());
		this.constraintProductResponseDto = new VoucherConstraintProductClientResDto(
			voucher.getVoucherConstraintProduct());
	}
	
}
