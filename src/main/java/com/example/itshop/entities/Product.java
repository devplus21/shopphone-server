package com.example.itshop.entities;

import com.example.itshop.enums.ProductStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "product")
@Getter
@Setter
@NoArgsConstructor
public class Product extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;
	
	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false)
	private String description;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "brand_id", nullable = false)
	@Fetch(FetchMode.JOIN)
	private Brand brand;
	
	@Column
	@Enumerated(EnumType.STRING)
	private ProductStatus status = ProductStatus.ACTIVE;
	
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<ProductVariant> productVariants = new LinkedHashSet<>();
	
	@OneToMany(mappedBy = "product")
	private Set<VoucherConstraintProductDetail> voucherConstraintProductDetails = new LinkedHashSet<>();
	
	@OneToOne(optional = false)
	@JoinColumn(name = "thumbnail", nullable = false)
	private File thumbnail;
	
	@OneToMany(mappedBy = "product")
	private Set<Review> reviews = new LinkedHashSet<>();
	
	public Product(Long id) {
		this.id = id;
	}
}