package com.example.itshop.entities;

import com.example.itshop.enums.VoucherConstraintProductType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Table(name = "voucher_constraint_product_detail")
@Getter
@Setter
@NoArgsConstructor
public class VoucherConstraintProductDetail {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "voucher_constraint_product_id", nullable = false)
	private VoucherConstraintProduct voucherConstraintProduct;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "product_id", nullable = false)
	private Product product;
}