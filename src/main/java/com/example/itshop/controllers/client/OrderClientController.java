package com.example.itshop.controllers.client;

import com.example.itshop.annotations.AppAuth;
import com.example.itshop.dto.client.request.PurchaseProductClientReqDto;
import com.example.itshop.dto.client.response.OrderClientResDto;
import com.example.itshop.dto.common.PaginationRequestDto;
import com.example.itshop.dto.common.PaginationResponseDto;
import com.example.itshop.enums.ClientType;
import com.example.itshop.services.client.OrderClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("${api.path.client}/order")
@AppAuth(clientTypes = ClientType.USER)
@RequiredArgsConstructor
public class OrderClientController {
	private final OrderClientService orderClientService;
	
	@GetMapping("all")
	public ResponseEntity<PaginationResponseDto<OrderClientResDto>> findAll(PaginationRequestDto dto) {
		return ResponseEntity.ok().body(orderClientService.findAll(dto));
	}
	
	@GetMapping("{id}")
	public ResponseEntity<OrderClientResDto> findById(@PathVariable Long id) {
		return ResponseEntity.ok().body(orderClientService.findById(id));
	}
	
	@PostMapping
	public ResponseEntity<OrderClientResDto> purchaseProduct(@RequestBody @Valid PurchaseProductClientReqDto dto) {
		return ResponseEntity.status(HttpStatus.CREATED).body(orderClientService.purchase(dto));
	}
}
