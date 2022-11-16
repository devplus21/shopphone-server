package com.example.itshop.dto.client.request;

import com.example.itshop.dto.common.PaginationRequestDto;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class GetProductReviewClientReqDto extends PaginationRequestDto {
      @NotNull
      private Long produtId;
}
