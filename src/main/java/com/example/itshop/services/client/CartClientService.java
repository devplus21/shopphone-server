package com.example.itshop.services.client;

import com.example.itshop.dto.client.request.AddCartClientReqDto;
import com.example.itshop.dto.client.request.UpdateCartClientReqDto;
import com.example.itshop.dto.client.response.CartClientResDto;
import com.example.itshop.entities.Cart;
import com.example.itshop.entities.ProductVariant;
import com.example.itshop.entities.User;
import com.example.itshop.repositories.CartRepository;
import com.example.itshop.repositories.ProductVariantRepository;
import com.example.itshop.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CartClientService {
	private final CartRepository cartRepo;
	private final ProductVariantRepository productVariantRepo;
	
	public List<CartClientResDto> getAllCart() {
		User currentUser = SecurityUtil.getCurrentUser();
		
		return cartRepo.findByUser(currentUser).stream()
			.map(CartClientResDto::new).collect(Collectors.toList());
	}
	
	public CartClientResDto addCart(AddCartClientReqDto dto) {
		ProductVariant productVariant = productVariantRepo.findById(dto.getProductVariantId())
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product variant not found"));
		if (productVariant.getStock() < dto.getQuantity()) {
			throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "Out of stock");
		}
		
		Cart cart = new Cart();
		cart.setProductVariant(productVariant);
		cart.setQuantity(dto.getQuantity());
		cart.setUser((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
		
		cartRepo.save(cart);
		return new CartClientResDto(cart);
	}
	
	public CartClientResDto updateCart(UpdateCartClientReqDto dto) {
		Cart cart = cartRepo.findById(dto.getCartId())
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart not found"));
		
		if (!cart.getUser().getId().equals(SecurityUtil.getCurrentUser().getId())) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Don't have right");
		}
		if (cart.getQuantity() + dto.getAmount() < 0) {
			throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "Quantity must greater than 0");
		}
		if (cart.getProductVariant().getStock() < cart.getQuantity() + dto.getAmount()) {
			throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "Out of Stock");
		}
		cart.setQuantity(cart.getQuantity() + dto.getAmount());
		cartRepo.save(cart);
		return new CartClientResDto(cart);
	}
	
	public Integer deleteCart(Long cartId) {
		User currentUser = SecurityUtil.getCurrentUser();
		return cartRepo.deleteByIdAndUser(cartId, currentUser);
	}
	
	
}
