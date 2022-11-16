package com.example.itshop.dto.admin.response;

import com.example.itshop.entities.VoucherConstraintUser;
import com.example.itshop.enums.VoucherConstraintUserType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VoucherConstraintUserAdminResDto {
	private Long id;
	
	private VoucherConstraintUserType constraintType;
	
	public VoucherConstraintUserAdminResDto(VoucherConstraintUser voucherConstraintUser) {
		this.id = voucherConstraintUser.getId();
		this.constraintType = voucherConstraintUser.getConstraintType();
	}
}
