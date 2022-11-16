package com.example.itshop.dto.client.response;

import com.example.itshop.entities.OrderDetail;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderDetailClientResDto {
	private Long id;
	private Double productPrice;
	private Long quantity;
	private ProductVariantClientResDto productVariantClientResDto;
	private VoucherClientResDto voucherClientResDto;
	private Double originalPrice;
	
	public OrderDetailClientResDto(OrderDetail orderDetail) {
		this.id = orderDetail.getId();
		this.productPrice = orderDetail.getProductPrice();
		this.quantity = orderDetail.getQuantity();
		this.productVariantClientResDto = new ProductVariantClientResDto(orderDetail.getProductVariant());
		this.voucherClientResDto = new VoucherClientResDto(orderDetail.getVoucher());
		this.originalPrice = orderDetail.getProductPrice() * orderDetail.getQuantity();
	}
}
