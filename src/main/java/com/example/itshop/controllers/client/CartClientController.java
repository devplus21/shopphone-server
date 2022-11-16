package com.example.itshop.controllers.client;

import com.example.itshop.annotations.AppAuth;
import com.example.itshop.dto.client.request.AddCartClientReqDto;
import com.example.itshop.dto.client.request.UpdateCartClientReqDto;
import com.example.itshop.dto.client.response.CartClientResDto;
import com.example.itshop.enums.ClientType;
import com.example.itshop.services.client.CartClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("${api.path.client}/cart")
@RequiredArgsConstructor
@AppAuth(clientTypes = ClientType.USER)
public class CartClientController {
	
	private final CartClientService cartClientService;
	
	@PostMapping
	public CartClientResDto addCart(@RequestBody @Valid AddCartClientReqDto dto) {
		return cartClientService.addCart(dto);
	}
	
	@GetMapping
	public List<CartClientResDto> getAllCart() {
		return cartClientService.getAllCart();
	}
	
	@PatchMapping
	public CartClientResDto updateCart(@RequestBody @Valid UpdateCartClientReqDto dto) {
		return cartClientService.updateCart(dto);
	}
	
	@DeleteMapping("{id}")
	public Integer deleteCart(@PathVariable Long id) {
		return cartClientService.deleteCart(id);
	}
}
