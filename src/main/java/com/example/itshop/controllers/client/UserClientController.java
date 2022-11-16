package com.example.itshop.controllers.client;

import com.example.itshop.annotations.AppAuth;
import com.example.itshop.dto.client.request.CreateUserClientReqDto;
import com.example.itshop.dto.client.request.UpdateUserClientReqDto;
import com.example.itshop.dto.client.request.UserLoginClientReqDto;
import com.example.itshop.dto.client.request.VerifyUserClientReqDto;
import com.example.itshop.dto.client.response.LoginClientResDto;
import com.example.itshop.dto.client.response.UserClientResDto;
import com.example.itshop.enums.ClientType;
import com.example.itshop.services.client.UserClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/${api.path.client}/user")
public class UserClientController {
	private final UserClientService userClientService;
	
	@GetMapping("/current")
	@AppAuth(clientTypes = ClientType.USER)
	public ResponseEntity<UserClientResDto> getCurrent() {
		return ResponseEntity.ok(userClientService.getCurrentUser());
	}
	
	@PostMapping("/register")
	public ResponseEntity<UserClientResDto> register(@RequestBody @Valid CreateUserClientReqDto dto) {
		return ResponseEntity.ok(userClientService.create(dto));
	}
	
	@PostMapping("/verify")
	public ResponseEntity<LoginClientResDto> verify(@RequestBody @Valid VerifyUserClientReqDto dto) {
		return ResponseEntity.ok(userClientService.verify(dto));
	}
	
	@PostMapping("/login")
	public ResponseEntity<LoginClientResDto> login(@RequestBody @Valid UserLoginClientReqDto dto) {
		return ResponseEntity.ok(userClientService.login(dto));
	}
	
	@PutMapping
	@AppAuth(clientTypes = ClientType.USER)
	public ResponseEntity<UserClientResDto> update(@RequestBody @Valid UpdateUserClientReqDto dto) {
		return ResponseEntity.ok(userClientService.update(dto));
	}
}
