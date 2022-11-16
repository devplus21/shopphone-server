package com.example.itshop.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "product_variant_image")
@Getter
@Setter
public class ProductVariantImage extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "product_variant_id", nullable = false)
	private ProductVariant productVariant;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "file_id", nullable = false)
	private File file;
	
}