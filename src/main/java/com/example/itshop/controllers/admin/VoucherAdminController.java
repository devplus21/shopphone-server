package com.example.itshop.controllers.admin;

import com.example.itshop.annotations.AppAuth;
import com.example.itshop.dto.admin.request.CreateVoucherAdminReqDto;
import com.example.itshop.dto.admin.request.UpdateVoucherAdminReqDto;
import com.example.itshop.dto.admin.request.VoucherSearchAdminReqDto;
import com.example.itshop.dto.admin.response.VoucherAdminResDto;
import com.example.itshop.dto.common.PaginationResponseDto;
import com.example.itshop.enums.ClientType;
import com.example.itshop.services.admin.VoucherAdminService;
import com.example.itshop.utils.CloudinaryUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("${api.path.admin}/voucher")
@RequiredArgsConstructor
@AppAuth(clientTypes = ClientType.ADMIN)
public class VoucherAdminController {
	private final VoucherAdminService voucherAdminService;
	private final CloudinaryUtil cloudinaryUtil;

//	@PostMapping(value = "file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//	public String upFile(@RequestParam("file") MultipartFile file) throws IOException {
//		System.out.println("upload file");
//		cloudinaryUtil.uploadFile(file);
//		return "123";
//	}
	
	@PostMapping
	public VoucherAdminResDto create(@RequestBody @Valid CreateVoucherAdminReqDto dto) {
		return voucherAdminService.create(dto);
	}
	
	@PatchMapping
	public VoucherAdminResDto update(@RequestBody @Valid UpdateVoucherAdminReqDto dto) {
		return voucherAdminService.update(dto);
	}
	
	@GetMapping("all")
	public PaginationResponseDto<VoucherAdminResDto> getAll(VoucherSearchAdminReqDto dto) {
		return voucherAdminService.getAll(dto);
	}
	
	@GetMapping("{id}")
	public VoucherAdminResDto getDetail(@PathVariable Long id) {
		return voucherAdminService.getDetail(id);
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity<?> deleteVoucher(@PathVariable Long id) {
		voucherAdminService.deleteVoucher(id);
		return ResponseEntity.ok(null);
	}
}
