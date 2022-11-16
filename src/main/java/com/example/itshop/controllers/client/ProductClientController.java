package com.example.itshop.controllers.client;

import com.example.itshop.dto.client.request.ProductSearchClientReqDto;
import com.example.itshop.dto.common.PaginationResponseDto;
import com.example.itshop.dto.client.response.ProductClientResDto;
import com.example.itshop.services.client.ProductClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping( "/${api.path.client}/product")
public class ProductClientController {
    private final ProductClientService productClientService;

    @GetMapping("/all")
    public PaginationResponseDto<ProductClientResDto> findAll(@Valid ProductSearchClientReqDto dto) {
          return  productClientService.findAll(dto);
    }

    @GetMapping("/{id}")
    public ProductClientResDto findById(@PathVariable long id) {
        return productClientService.findById(id);
    }

}
