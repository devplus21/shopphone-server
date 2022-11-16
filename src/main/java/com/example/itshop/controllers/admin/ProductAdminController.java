package com.example.itshop.controllers.admin;

import com.example.itshop.annotations.AppAuth;
import com.example.itshop.dto.admin.request.*;
import com.example.itshop.dto.admin.response.ProductAdminResDto;
import com.example.itshop.dto.admin.response.ProductVariantAdminResDto;
import com.example.itshop.dto.common.PaginationResponseDto;
import com.example.itshop.enums.ClientType;
import com.example.itshop.services.admin.ProductAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@AppAuth(clientTypes = ClientType.ADMIN)
@RequestMapping("${api.path.admin}/product")
public class ProductAdminController {
	
	private final ProductAdminService productAdminService;
	
	@GetMapping("all")
	public ResponseEntity<PaginationResponseDto<ProductAdminResDto>> findAll(ProductSearchAdminReqDto dto) {
		return ResponseEntity.ok().body(productAdminService.findAll(dto));
	}
	
	@GetMapping("{id}")
	public ResponseEntity<ProductAdminResDto> findById(@PathVariable Long id) {
		return ResponseEntity.ok().body(productAdminService.findById(id));
	}
	
	@PostMapping("variant")
	public ResponseEntity<ProductVariantAdminResDto> createProductVariant(@RequestBody @Valid CreateProductVariantAdminReqDto dto) {
		return ResponseEntity.status(HttpStatus.CREATED).body(productAdminService.createProductVariant(dto));
	}
	
	@PostMapping
	public ResponseEntity<ProductAdminResDto> create(@RequestBody @Valid CreateProductAdminReqDto dto) {
		return ResponseEntity.status(HttpStatus.CREATED).body(productAdminService.create(dto));
	}
	
	@PutMapping("variant")
	public ResponseEntity<ProductVariantAdminResDto> updateProductVariant(@RequestBody @Valid UpdateProductVariantAdminReqDto dto) {
		return ResponseEntity.ok().body(productAdminService.updateProductVariant(dto));
	}
	
	@PutMapping
	public ResponseEntity<ProductAdminResDto> updateProduct(@RequestBody @Valid UpdateProductAdminReqDto dto) {
		return ResponseEntity.ok().body(productAdminService.updateProductInfo(dto));
	}
	
	@DeleteMapping("variant/{id}")
	public ResponseEntity<?> deleteProductVariant(@PathVariable Long id) {
		productAdminService.deleteProductVariant(id);
		return ResponseEntity.ok(null);
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
		productAdminService.deleteProduct(id);
		return ResponseEntity.ok(null);
	}
}
