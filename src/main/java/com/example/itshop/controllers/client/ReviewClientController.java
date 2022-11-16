package com.example.itshop.controllers.client;

import com.example.itshop.dto.client.request.CreateReviewClientReqDto;
import com.example.itshop.dto.client.request.EditReviewClientReqDto;
import com.example.itshop.dto.client.request.GetProductReviewClientReqDto;
import com.example.itshop.dto.client.response.ReviewClientResDto;
import com.example.itshop.dto.common.PaginationRequestDto;
import com.example.itshop.dto.common.PaginationResponseDto;
import com.example.itshop.services.client.ReviewClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.path.client}/review")
public class ReviewClientController {
	private final ReviewClientService reviewClientService;
	
	@GetMapping("product")
	public ResponseEntity<PaginationResponseDto<ReviewClientResDto>> findAll(GetProductReviewClientReqDto dto) {
		return ResponseEntity.ok().body(reviewClientService.findAll(dto));
	}
	
	@PostMapping
	public ResponseEntity<ReviewClientResDto> create(@RequestBody @Valid CreateReviewClientReqDto dto) {
		return ResponseEntity.ok().body(reviewClientService.createReview(dto));
	}
	
	@PatchMapping
	public ResponseEntity<ReviewClientResDto> create(@RequestBody @Valid EditReviewClientReqDto dto) {
		return ResponseEntity.ok().body(reviewClientService.editReview(dto));
	}
}
