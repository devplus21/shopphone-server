package com.example.itshop.entities;

import com.example.itshop.enums.VoucherStatus;
import com.example.itshop.enums.VoucherType;
import com.example.itshop.enums.VoucherUnit;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "voucher")
@Getter
@Setter
public class Voucher extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;
	
	@Column(nullable = false)
	private String name;
	
	@Column(name = "discount_amount", nullable = false)
	private Double discountAmount;
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private VoucherUnit unit;
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private VoucherType type;
	
	@Column(nullable = false, name = "starts_at")
	private OffsetDateTime startsAt;
	
	@Column(nullable = false, name = "expires_at")
	private OffsetDateTime expiresAt;
	
	@Column(nullable = false)
	private Long quantity;
	
	@Column(name = "used_amount", nullable = false)
	private Long usedAmount  = 0L;
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private VoucherStatus status = VoucherStatus.ACTIVE;
	
	@OneToMany(mappedBy = "voucher", orphanRemoval = true)
	private Set<OrderDetail> orderDetails = new LinkedHashSet<>();
	
	@OneToMany(mappedBy = "voucher", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<UserVoucher> userVouchers = new LinkedHashSet<>();
	
	@OneToMany(mappedBy = "voucher")
	private Set<Order> orders = new LinkedHashSet<>();
	
	@OneToOne(mappedBy = "voucher", cascade = CascadeType.ALL, orphanRemoval = true)
	private VoucherConstraintUser voucherConstraintUser;
	
	@OneToOne(mappedBy = "voucher", cascade = CascadeType.ALL, orphanRemoval = true)
	private VoucherConstraintProduct voucherConstraintProduct;
	
}