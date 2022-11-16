package com.example.itshop.dto.admin.request;

import lombok.Data;

@Data
public class UpdateAccountAdminReqDto {
	private Long id;
	private String name;
	private String currentPassword;
	private String newPassword;
}
