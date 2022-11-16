package com.example.itshop.entities;

import com.example.itshop.enums.ReviewStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "review")
@Getter
@Setter
public class Review extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;
	
	private ReviewStatus status = ReviewStatus.PENDING;
	
	private Integer rating;
	
	private String comment;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "product_id", nullable = false)
	private Product product;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "order_id", nullable = false)
	private Order order;
}