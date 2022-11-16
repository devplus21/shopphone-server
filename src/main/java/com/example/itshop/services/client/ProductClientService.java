package com.example.itshop.services.client;

import com.example.itshop.dto.client.request.ProductSearchClientReqDto;
import com.example.itshop.dto.common.PaginationResponseDto;
import com.example.itshop.dto.client.response.ProductClientResDto;
import com.example.itshop.entities.Product;
import com.example.itshop.enums.ProductAttributeCode;
import com.example.itshop.enums.SortType;
import com.example.itshop.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductClientService {
    private final ProductRepository productRepo;

    public PaginationResponseDto<ProductClientResDto> findAll(ProductSearchClientReqDto dto) {
        Page<Product> productPage = productRepo.findAll((root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.hasText(dto.getBrand())) {
                predicates.add(builder.equal(root.get("brand").get("code"), dto.getBrand()));
            }

            if (StringUtils.hasText(dto.getCpu())) {
                predicates.add(builder.equal(
                        root.join("productVariants", JoinType.INNER)
                                .join("productAttributes", JoinType.INNER)
                                .get("code"),
                        ProductAttributeCode.CPU
                ));
                predicates.add(builder.equal(
                        root.join("productVariants", JoinType.INNER)
                                .join("productAttributes", JoinType.INNER)
                                .get("value"),
                        dto.getCpu()
                ));
            }

            if (StringUtils.hasText(dto.getRam())) {
                predicates.add(builder.equal(
                        root.join("productVariants", JoinType.INNER)
                                .join("productAttributes", JoinType.INNER)
                                .get("code"),
                        ProductAttributeCode.RAM
                ));
                predicates.add(builder.equal(
                        root.join("productVariants", JoinType.INNER)
                                .join("productAttributes", JoinType.INNER)
                                .get("value"),
                        dto.getRam()
                ));
            }

            if (StringUtils.hasText(dto.getRom())) {
                predicates.add(builder.equal(
                        root.join("productVariants", JoinType.INNER)
                                .join("productAttributes", JoinType.INNER)
                                .get("code"),
                        ProductAttributeCode.ROM
                ));
                predicates.add(builder.equal(
                        root.join("productVariants", JoinType.INNER)
                                .join("productAttributes", JoinType.INNER)
                                .get("value"),
                        dto.getRom()
                ));
            }

            if (Objects.nonNull(dto.getMinPrice())) {
                predicates.add(builder.equal(
                        root.join("productVariants", JoinType.INNER).get("price"),
                        dto.getMinPrice()
                ));
            }

            if (Objects.nonNull(dto.getMaxPrice())) {
                predicates.add(builder.equal(
                        root.join("productVariants", JoinType.INNER).get("price"),
                        dto.getMaxPrice()
                ));
            }

            if (Objects.nonNull(dto.getSortBy())) {
                if (Objects.nonNull(dto.getSortType()) && dto.getSortType().equals(SortType.DESC)) {
                    switch (dto.getSortBy()) {
                        case NAME -> query.orderBy(builder.desc(root.get("name")));
                        case PRICE -> query.orderBy(builder.desc(
                                root.join("productVariants").get("price")
                        ));
                        case RECENT -> query.orderBy(builder.desc(root.get("updatedAt")));
                    }
                }
                switch (dto.getSortBy()) {
                    case NAME -> query.orderBy(builder.asc(root.get("name")));
                    case PRICE -> query.orderBy(builder.asc(
                            root.join("productVariants").get("price")
                    ));
                    case RECENT -> query.orderBy(builder.asc(root.get("updatedAt")));
                }
            }

            return query.where(predicates.toArray(Predicate[]::new)).getRestriction();
        }, dto.getPageable());

        List<ProductClientResDto> responseDtos = productPage.map(ProductClientResDto::new).stream().toList();

        return new PaginationResponseDto<>(responseDtos, productPage);
    }

    public ProductClientResDto findById(long id) {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
        return new ProductClientResDto(product);
    }
}
