package com.example.itshop.controllers.admin;

import com.example.itshop.annotations.AppAuth;
import com.example.itshop.dto.admin.request.AccountSearchAdminReqDto;
import com.example.itshop.dto.admin.request.CreateAccountAdminReqDto;
import com.example.itshop.dto.admin.request.LoginAdminReqDto;
import com.example.itshop.dto.admin.request.UpdateAccountAdminReqDto;
import com.example.itshop.dto.admin.response.AccountAdminResDto;
import com.example.itshop.dto.admin.response.LoginAdminResDto;
import com.example.itshop.dto.common.PaginationResponseDto;
import com.example.itshop.enums.ClientType;
import com.example.itshop.services.admin.AdminAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.path.admin}/account")
public class AdminAccountController {
	private final AdminAccountService adminAccountService;
	
	@AppAuth(clientTypes = ClientType.ADMIN)
	@GetMapping("all")
	public ResponseEntity<PaginationResponseDto<AccountAdminResDto>> getAll(AccountSearchAdminReqDto dto) {
		return ResponseEntity.ok(adminAccountService.getAll(dto));
	}
	
	@AppAuth(clientTypes = ClientType.ADMIN)
	@GetMapping("current")
	public ResponseEntity<AccountAdminResDto> current() {
		return ResponseEntity.ok(adminAccountService.getCurrent());
	}
	
	@AppAuth(clientTypes = ClientType.ADMIN)
	@GetMapping("{id}")
	public ResponseEntity<AccountAdminResDto> getDetail(@PathVariable Long id) {
		return ResponseEntity.ok(adminAccountService.getDetail(id));
	}
	
	@PostMapping("login")
	public ResponseEntity<LoginAdminResDto> login(@RequestBody @Valid LoginAdminReqDto dto) {
		return ResponseEntity.ok(adminAccountService.login(dto));
	}
	
	
	@AppAuth(clientTypes = ClientType.ADMIN)
	@PostMapping
	public ResponseEntity<AccountAdminResDto> create(@RequestBody @Valid CreateAccountAdminReqDto dto) {
		return ResponseEntity.status(HttpStatus.CREATED).body(adminAccountService.create(dto));
	}
	
	@AppAuth(clientTypes = ClientType.ADMIN)
	@PatchMapping
	public ResponseEntity<AccountAdminResDto> update(@RequestBody @Valid UpdateAccountAdminReqDto dto) {
		return ResponseEntity.ok(adminAccountService.update(dto));
	}
	
	@AppAuth(clientTypes = ClientType.ADMIN)
	@DeleteMapping("{id}")
	public ResponseEntity<?> deleteAccount(@PathVariable Long id) {
		adminAccountService.deleteAdmin(id);
		return ResponseEntity.ok(null);
	}
}
