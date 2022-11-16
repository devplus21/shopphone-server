package com.example.itshop.dto.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginationResponseDto<T> {

    private Pagination pagination;
    private Collection<T> data;

    public PaginationResponseDto(Collection<T> dataList, Page<?> dataPage) {
       this.data = dataList;
       this.pagination = new Pagination(dataPage);
    }


    @Data
    public static final class Pagination {
        private Integer page;
        private Integer size;
        private Boolean isLastPage;
        private Long totalItems;
        private Integer totalPages;

        public Pagination(Page<?> dataToConvert)  {
            this.isLastPage = dataToConvert.isLast();
            this.page = dataToConvert.getPageable().getPageNumber() + 1;
            this.size = dataToConvert.getPageable().getPageSize();
            this.totalItems = dataToConvert.getTotalElements();
            this.totalPages = dataToConvert.getTotalPages();
        }
    }
}
