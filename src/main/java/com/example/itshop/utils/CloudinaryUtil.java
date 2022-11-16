package com.example.itshop.utils;

import com.cloudinary.Cloudinary;
import com.example.itshop.entities.File;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CloudinaryUtil {
	private final Cloudinary cloudinary;
	@Value("${cloudinary.preset}")
	private String preset;
	
	public File uploadFile(MultipartFile multipartFile) throws IOException {
		java.io.File uploadFile = this.convertMultiPartToFile(multipartFile);
		Map<String, String> result = cloudinary.uploader()
			.upload(uploadFile, Map.of("upload_preset", preset));
		File file = new File();
		file.setPublicId(result.get("public_id"));
		file.setResourceType(result.get("resource_type"));
		file.setUrl(result.get("secure_url"));
		return file;
	}
	
	public void deleteFile(String publicId) throws IOException {
		cloudinary.uploader().destroy(publicId, Map.of());
	}
	
	private java.io.File convertMultiPartToFile(MultipartFile multipartFile) throws IOException {
		java.io.File convertFile = new java.io.File(multipartFile.getOriginalFilename());
		FileOutputStream fos = new FileOutputStream(convertFile);
		fos.write(multipartFile.getBytes());
		fos.close();
		return convertFile;
	}
}
