package com.example.itshop.entities;

import com.example.itshop.enums.OrderStatus;
import com.example.itshop.enums.VoucherUnit;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private OrderStatus status;
	
	@Column(nullable = false, name = "total_price")
	private Double totalPrice;
	
	@OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST, orphanRemoval = true)
	private Set<OrderDetail> orderDetails = new LinkedHashSet<>();
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
	
	@ManyToOne
	@JoinColumn(name = "voucher_id")
	private Voucher voucher;
	
	@Column(name = "discount_amount", nullable = false)
	private Double discountAmount;
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private VoucherUnit unit;
	
	@OneToMany(mappedBy = "order")
	private Set<Review> reviews = new LinkedHashSet<>();
	
}