package com.example.itshop.dto.admin.response;

import com.example.itshop.entities.Admin;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class AccountAdminResDto {
	private Long id;
	private String email;
	private OffsetDateTime createdAt;
	private OffsetDateTime updatedAt;
	private String name;
	
	public AccountAdminResDto(Admin admin) {
		this.id = admin.getId();
		this.email = admin.getEmail();
		this.createdAt = admin.getCreatedAt();
		this.updatedAt = admin.getUpdatedAt();
		this.name = admin.getName();
	}
}
