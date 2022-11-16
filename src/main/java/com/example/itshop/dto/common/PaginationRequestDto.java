package com.example.itshop.dto.common;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Data
public class PaginationRequestDto {
	@Schema(defaultValue = "1")
	private int page = 1;
	@Schema(defaultValue = "20")
	private int size = 20;
	
	@Schema(hidden = true)
	public Pageable getPageable() {
		return PageRequest.of(page - 1, size);
	}
}
