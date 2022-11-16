package com.example.itshop.dto.admin.request;

import com.example.itshop.enums.ProductAttributeCode;
import com.example.itshop.enums.ProductStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.*;
import java.util.Collection;

@Data
public class UpdateProductVariantAdminReqDto {
     @NotNull
     private Long productVariantId;
     
     @PositiveOrZero
     @NotNull
     private Double price;
     
     @Positive
     private Long stock;
     
     @JsonProperty(value = "attributes")
     @Size(min = 5, max = 5)
     private Collection<ProductAttributeDto> attributeDtos;
     
     @NotEmpty
     private Collection<Long> imageDtos;
     
     @Data
     public static class ProductAttributeDto {
          @NotNull
          private Long id;
          
          @NotNull
          private ProductAttributeCode code;
          
          @NotBlank
          private String value;
     }
}
