package com.example.itshop.dto.client.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Data
public class UpdateCartClientReqDto {
    @NotNull
    private Long cartId;
    
    @Positive
    private Long amount;
}
