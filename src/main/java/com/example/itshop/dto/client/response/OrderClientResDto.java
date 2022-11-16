package com.example.itshop.dto.client.response;

import com.example.itshop.entities.Order;
import com.example.itshop.enums.OrderStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class OrderClientResDto {
	private Long id;
	private OrderStatus status;
	private Double totalPrice;
	private List<OrderDetailClientResDto> orderDetailClientResDtoList;
	private OffsetDateTime createdAt;
	private OffsetDateTime updatedAt;
	private Double originalPrice;
	
	public OrderClientResDto(Order order) {
		this.id = order.getId();
		this.status = order.getStatus();
		this.totalPrice = order.getTotalPrice();
		this.orderDetailClientResDtoList = order.getOrderDetails().stream()
			.map(OrderDetailClientResDto::new).collect(Collectors.toList());
		this.createdAt = order.getCreatedAt();
		this.updatedAt = order.getUpdatedAt();
		this.originalPrice = order.getOrderDetails().stream()
			.map(item -> item.getProductPrice() * item.getQuantity())
			.reduce(0D, Double::sum);
	}
}
