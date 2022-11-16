package com.example.itshop.dto.client.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
public class PurchaseProductClientReqDto {
    @JsonProperty("vouchers")
    private List<VoucherDto> voucherDtos =  new ArrayList<>();
    
    @JsonProperty("voucherAllId")
    private Long voucherAllId;
    
    @Data
    public static class VoucherDto {
        @NotNull
        private Long userVoucherId;
        
        @NotNull
        private Long cartId;
    }
}
