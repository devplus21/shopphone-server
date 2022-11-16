package com.example.itshop.dto.admin.request;

import com.example.itshop.enums.VoucherConstraintProductType;
import com.example.itshop.enums.VoucherConstraintUserType;
import com.example.itshop.enums.VoucherType;
import com.example.itshop.enums.VoucherUnit;
import lombok.Data;
import net.bytebuddy.implementation.bind.annotation.SuperCall;

import java.util.List;

@Data
public class UpdateVoucherAdminReqDto extends CreateVoucherAdminReqDto {
	private Long id;
}
