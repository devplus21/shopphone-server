package com.example.itshop.dto.common;

import com.example.itshop.entities.File;
import lombok.Data;

@Data
public class FileResponseDto {
	private Long id;
	private String resourceType;
	
	private String publicId;
	
	private String url;
	
	public FileResponseDto(File file) {
		this.id = file.getId();
		this.resourceType = file.getResourceType();
		this.publicId = file.getPublicId();
		this.url = file.getUrl();
	}
}
