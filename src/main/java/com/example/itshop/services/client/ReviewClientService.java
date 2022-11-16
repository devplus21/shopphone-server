package com.example.itshop.services.client;

import com.example.itshop.dto.client.request.CreateReviewClientReqDto;
import com.example.itshop.dto.client.request.EditReviewClientReqDto;
import com.example.itshop.dto.client.request.GetProductReviewClientReqDto;
import com.example.itshop.dto.client.response.ReviewClientResDto;
import com.example.itshop.dto.common.PaginationResponseDto;
import com.example.itshop.entities.Product_;
import com.example.itshop.entities.Review;
import com.example.itshop.entities.Review_;
import com.example.itshop.entities.User;
import com.example.itshop.enums.ReviewStatus;
import com.example.itshop.repositories.ReviewRepository;
import com.example.itshop.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.criteria.Predicate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewClientService {
	
	private final ReviewRepository reviewRepo;
	
	public ReviewClientResDto createReview(CreateReviewClientReqDto dto) {
		User user = SecurityUtil.getCurrentUser();
		Review review = reviewRepo.findFirstByOrder_User_IdAndStatus(
				user.getId(), ReviewStatus.PENDING)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Review not found"));
		review.setComment(dto.getComment());
		review.setRating(dto.getRating());
		review.setCreatedAt(OffsetDateTime.now());
		review.setStatus(ReviewStatus.COMPLETE);
		reviewRepo.save(review);
		return new ReviewClientResDto(review);
	}
	
	public PaginationResponseDto<ReviewClientResDto> findAll(GetProductReviewClientReqDto dto) {
		Page<Review> reviewPage = reviewRepo.findAll((root, query, builder) -> {
			List<Predicate> predicates = new ArrayList<>();
			predicates.add(builder.equal(
				root.get(Review_.PRODUCT).get(Product_.ID), dto.getProdutId()
			));
			return query.where(predicates.toArray(new Predicate[0])).getRestriction();
		}, dto.getPageable());
		
		List<ReviewClientResDto> reviewClientResDtos = reviewPage.map(ReviewClientResDto::new)
			.stream().toList();
		return new PaginationResponseDto<>(reviewClientResDtos, reviewPage);
	}
	
	public ReviewClientResDto editReview(EditReviewClientReqDto dto) {
		Review review = reviewRepo.findById(dto.getId())
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Review not found"));
		review.setComment(dto.getComment());
		review.setRating(dto.getRating());
		reviewRepo.save(review);
		return new ReviewClientResDto(review);
	}
}
