package com.example.itshop.dto.admin.request;

import com.example.itshop.constants.AppConstant;
import com.example.itshop.enums.VoucherConstraintProductType;
import com.example.itshop.enums.VoucherConstraintUserType;
import com.example.itshop.enums.VoucherType;
import com.example.itshop.enums.VoucherUnit;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@Data
public class CreateVoucherAdminReqDto {
	@NotBlank
	private String name;
	@Positive
	private Double discountAmount;
	@NotNull
	private VoucherUnit unit;
	@NotBlank
	private String startsAt;
	@NotBlank
	private String expiresAt;
	@NotNull
	private VoucherType voucherType;
	@Positive
	private Long quantity;
	@NotNull
	private VoucherConstraintUserType constraintUserType;
	@NotNull
	private VoucherConstraintProductType constraintProductType;
	private Long brandId;
	private List<Long> productIds;
}
