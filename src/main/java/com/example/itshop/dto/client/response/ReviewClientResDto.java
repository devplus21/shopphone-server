package com.example.itshop.dto.client.response;

import com.example.itshop.entities.Review;
import com.example.itshop.enums.ReviewStatus;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class ReviewClientResDto {
	private Long id;
	private ReviewStatus status;
	private Integer rating;
	private String comment;
	private OffsetDateTime createdAt;
	private OffsetDateTime updatedAt;
	private OrderClientResDto orderClientResDto;
	
	public ReviewClientResDto(Review review) {
		this.id = review.getId();
		this.status = review.getStatus();
		this.rating = review.getRating();
		this.comment = review.getComment();
		this.createdAt = review.getCreatedAt();
		this.updatedAt = review.getUpdatedAt();
		this.orderClientResDto = new OrderClientResDto(review.getOrder());
	}
}
