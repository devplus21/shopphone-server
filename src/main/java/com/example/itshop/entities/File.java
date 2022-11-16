package com.example.itshop.entities;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "files")
@Getter
@Setter
@NoArgsConstructor
public class File extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;
	
	@Column(name = "resource_type")
	private String resourceType;
	
	@Column(name = "public_id")
	private String publicId;
	
	private String url;
	
	@OneToOne(mappedBy = "avatar", fetch = FetchType.LAZY)
	private User user;
	
	@OneToMany(mappedBy = "file")
	private Set<ProductVariantImage> productVariantImages = new LinkedHashSet<>();
	
	@OneToOne(mappedBy = "thumbnail", optional = false, fetch = FetchType.LAZY)
	private Product product;
}