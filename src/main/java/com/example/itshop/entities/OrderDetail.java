package com.example.itshop.entities;

import com.example.itshop.enums.VoucherUnit;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "order_detail")
@Getter
@Setter
public class OrderDetail {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;
	
	@Column(nullable = false, name = "product_price")
	private Double productPrice;
	
	@Column(nullable = false)
	private Long quantity;
	
	@Column(name = "discount_amount", nullable = false)
	private Double discountAmount;
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private VoucherUnit unit;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "order_id", nullable = false)
	@Fetch(FetchMode.JOIN)
	private Order order;
	
	@ManyToOne
	@JoinColumn(name = "voucher_id")
	@Fetch(FetchMode.JOIN)
	private Voucher voucher;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "product_variant_id", nullable = false)
	@Fetch(FetchMode.JOIN)
	private ProductVariant productVariant;
}