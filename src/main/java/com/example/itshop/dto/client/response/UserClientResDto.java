package com.example.itshop.dto.client.response;

import com.example.itshop.dto.common.FileResponseDto;
import com.example.itshop.entities.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
public class UserClientResDto implements Serializable {
	private OffsetDateTime createdAt;
	private OffsetDateTime updatedAt;
	private Long id;
	private String email;
	private String name;
	private FileResponseDto avatar;
	
	public UserClientResDto(User user) {
		this.createdAt = user.getCreatedAt();
		this.updatedAt = user.getUpdatedAt();
		this.id = user.getId();
		this.email = user.getEmail();
		this.name = user.getName();
		if (Objects.nonNull(user.getAvatar())) {
			this.avatar = new FileResponseDto(user.getAvatar());
		}
	}
}
