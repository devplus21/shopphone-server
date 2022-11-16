package com.example.itshop.dto.client.request;

import com.example.itshop.dto.common.PaginationRequestDto;
import com.example.itshop.enums.ProductSortProperty;
import com.example.itshop.enums.SortType;
import lombok.Data;

@Data
public class ProductSearchClientReqDto extends PaginationRequestDto {
    private String brand;

    private String ram;

    private String cpu;

    private String rom;

    private Long maxPrice;

    private Long minPrice;

    private ProductSortProperty sortBy;

    private SortType sortType;
}
