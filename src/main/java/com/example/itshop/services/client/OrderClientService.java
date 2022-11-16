package com.example.itshop.services.client;

import com.example.itshop.dto.client.request.PurchaseProductClientReqDto;
import com.example.itshop.dto.client.response.OrderClientResDto;
import com.example.itshop.dto.common.PaginationRequestDto;
import com.example.itshop.dto.common.PaginationResponseDto;
import com.example.itshop.entities.*;
import com.example.itshop.enums.OrderStatus;
import com.example.itshop.enums.VoucherUnit;
import com.example.itshop.repositories.*;
import com.example.itshop.types.Pair;
import com.example.itshop.types.Triple;
import com.example.itshop.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.orm.hibernate5.HibernateTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderClientService {
	private final OrderRepository orderRepo;
	private final VoucherRepository voucherRepo;
	private final CartRepository cartRepo;
	private final ProductRepository productRepo;
	private final ProductVariantRepository productVariantRepo;
	private final UserVoucherRepository userVoucherRepo;
	private final ReviewRepository reviewRepo;
	
	public OrderClientResDto purchase(PurchaseProductClientReqDto dto) {
		User user = SecurityUtil.getCurrentUser();
		List<Cart> carts = cartRepo.findByUser(user);
		List<UserVoucher> userVouchers = new ArrayList<>();
		UserVoucher userVoucherAll = null;
		Order order = new Order();
		List<OrderDetail> orderDetails = new ArrayList<>();
		List<Triple<Cart, ProductVariant, UserVoucher>> tripleList = new ArrayList<>();
		double totalPrice = 0D;
		List<Review> reviews = new ArrayList<>();
		
		//		lock product, lock user voucher
		List<ProductVariant> productVariants = productVariantRepo.findByIdsAndLock(carts.stream().map(item -> item.getProductVariant().getId()).collect(Collectors.toList()));
		
		if (!CollectionUtils.isEmpty(dto.getVoucherDtos())) {
			userVouchers = userVoucherRepo.findByIdsAndLock(dto.getVoucherDtos().stream().map(item -> item.getUserVoucherId()).collect(Collectors.toList()));
		}
		
		if (Objects.nonNull(dto.getVoucherAllId())) {
			userVoucherAll = userVoucherRepo.findByIdAndLock(dto.getVoucherAllId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User voucher not found"));
		}
		
		//		add triple
		for (Cart cart : carts) {
			ProductVariant productVariant = productVariants.stream().filter(item -> item.getId().equals(cart.getProductVariant().getId())).findFirst().orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error in add triple"));
			UserVoucher userVoucher = null;
			
			PurchaseProductClientReqDto.VoucherDto voucherDto = dto.getVoucherDtos().stream().filter(item -> item.getCartId().equals(cart.getId())).findFirst().orElse(null);
			
			if (Objects.nonNull(voucherDto)) {
				userVoucher = userVouchers.stream().filter(item -> item.getId().equals(voucherDto.getUserVoucherId())).findFirst().orElse(null);
			}
			
			tripleList.add(Triple.of(cart, productVariant, userVoucher));
		}
		
		//		check product, check user voucher
		if (Objects.nonNull(dto.getVoucherAllId()) && userVoucherAll.isUsed())
			throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "Voucher used");
		
		tripleList.forEach(triple -> {
			Cart cart = triple.getFirst();
			ProductVariant productVariant = triple.getSecond();
			UserVoucher userVoucher = triple.getThird();
			
			if (cart.getQuantity() > productVariant.getStock())
				throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "Out of Stock");
			
			if (Objects.nonNull(userVoucher) && userVoucher.isUsed())
				throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "Voucher used");
		});
		
		//		insert order
		for (Triple<Cart, ProductVariant, UserVoucher> item : tripleList) {
			Cart cart = item.getFirst();
			ProductVariant productVariant = item.getSecond();
			UserVoucher userVoucher = item.getThird();
			
			Double orderDetailPrice = cart.getQuantity() * productVariant.getPrice();
			
			OrderDetail orderDetail = new OrderDetail();
			orderDetail.setOrder(order);
			orderDetail.setProductVariant(productVariant);
			orderDetail.setProductPrice(productVariant.getPrice());
			orderDetail.setQuantity(cart.getQuantity());
			
			if (Objects.nonNull(userVoucher)) {
				orderDetail.setVoucher(userVoucher.getVoucher());
				orderDetail.setDiscountAmount(userVoucher.getVoucher().getDiscountAmount());
				orderDetail.setUnit(userVoucher.getVoucher().getUnit());
				
				switch (orderDetail.getUnit()) {
					case VND -> orderDetailPrice -= orderDetail.getDiscountAmount();
					case PERCENT -> orderDetailPrice -= (orderDetailPrice * orderDetail.getDiscountAmount()) / 100;
				}
				userVoucher.setUsed(true);
			}
			
			order.getOrderDetails().add(orderDetail);
			
			totalPrice += orderDetailPrice;
			
			//		reduce product quantity
			productVariant.setStock(productVariant.getStock() - cart.getQuantity());

//			Add review
			Review review = new Review();
			review.setOrder(order);
			review.setProduct(productVariant.getProduct());
			reviews.add(review);
		}
		
		if (Objects.nonNull(userVoucherAll.getVoucher())) {
			order.setDiscountAmount(userVoucherAll.getVoucher().getDiscountAmount());
			order.setVoucher(userVoucherAll.getVoucher());
			order.setUnit(userVoucherAll.getVoucher().getUnit());
			
			switch (order.getUnit()) {
				case VND -> totalPrice -= totalPrice;
				case PERCENT -> totalPrice -= (totalPrice * order.getDiscountAmount()) / 100;
			}
			
			userVoucherAll.setUsed(true);
			userVoucherRepo.save(userVoucherAll);
		}
		
		order.setStatus(OrderStatus.SUCCESS);
		order.setUser(user);
		order.setTotalPrice(totalPrice);
		
		orderRepo.save(order);
		userVoucherRepo.saveAll(userVouchers);
		cartRepo.deleteAll(carts);
		productVariantRepo.saveAll(productVariants);
		reviewRepo.saveAll(reviews);
		
		return new OrderClientResDto(order);
	}
	
	public PaginationResponseDto<OrderClientResDto> findAll(PaginationRequestDto dto) {
		Page<Order> orderPage = orderRepo.findAll((root, query, criteriaBuilder) -> {
			return query.getRestriction();
		}, dto.getPageable());
		
		List<OrderClientResDto> orderClientResDtos = orderPage.map(OrderClientResDto::new).stream().toList();
		return new PaginationResponseDto<>(orderClientResDtos, orderPage);
	}
	
	public OrderClientResDto findById(Long id) {
		Order order = orderRepo.findById(id)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
		return new OrderClientResDto(order);
	}
}
