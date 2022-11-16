package com.example.itshop.dto.client.response;

import com.example.itshop.entities.VoucherConstraintUser;
import com.example.itshop.enums.VoucherConstraintUserType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VoucherConstraintUserClientResDto {
	private Long id;
	
	private VoucherConstraintUserType constraintType;
	
	public VoucherConstraintUserClientResDto(VoucherConstraintUser voucherConstraintUser) {
		this.id = voucherConstraintUser.getId();
		this.constraintType = voucherConstraintUser.getConstraintType();
	}
}
