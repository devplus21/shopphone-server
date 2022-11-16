package com.example.itshop.services.admin;

import com.example.itshop.dto.admin.request.*;
import com.example.itshop.dto.admin.response.ProductAdminResDto;
import com.example.itshop.dto.admin.response.ProductVariantAdminResDto;
import com.example.itshop.dto.common.PaginationResponseDto;
import com.example.itshop.entities.*;
import com.example.itshop.repositories.*;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.criteria.Predicate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductAdminService {
	private final ProductRepository productRepo;
	private final ProductVariantRepository productVariantRepo;
	private final ProductAttributeRepository productAttributeRepo;
	private final FileRepository fileRepo;
	private final BrandRepository brandRepo;
	
	public PaginationResponseDto<ProductAdminResDto> findAll(ProductSearchAdminReqDto dto) {
		Page<Product> productPage = productRepo.findAll((root, query, builder) -> {
			List<Predicate> predicates = new ArrayList<>();
			if (StringUtils.hasText(dto.getSearchText())) {
				String searchText = "%" + dto.getSearchText() + "%";
				List<Predicate> predicatesSearchText = new ArrayList<>();
				predicatesSearchText.add(builder.equal(
					root.get(Product_.NAME), searchText
				));
				predicatesSearchText.add(builder.equal(
					root.get(Product_.DESCRIPTION), searchText
				));
				predicates.add(builder.or(predicatesSearchText.toArray(new Predicate[0])));
			}
			
			if (Objects.nonNull(dto.getBrandId())) {
				predicates.add(builder.equal(
					root.get(Product_.BRAND).get(Brand_.ID), dto.getBrandId()
				));
			}
			
			if (Objects.nonNull(dto.getStatus())) {
				predicates.add(builder.equal(
					root.get(Product_.STATUS), dto.getStatus()
				));
			}
			
			return query.where(predicates.toArray(new Predicate[0])).getRestriction();
		}, dto.getPageable());
		
		List<ProductAdminResDto> adminReqDtos = productPage.map(
			product -> new ProductAdminResDto(product, product.getProductVariants())).stream().toList();
		
		return new PaginationResponseDto<>(adminReqDtos, productPage);
	}
	
	public ProductAdminResDto findById(Long id) {
		Product product = productRepo.findById(id)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Prodcut not found"));
		return new ProductAdminResDto(product, product.getProductVariants());
	}
	
	public ProductAdminResDto create(CreateProductAdminReqDto dto) {
		Product product = new Product();
		
		product.setName(dto.getName());
		product.setDescription(dto.getDescription());
		product.setStatus(dto.getStatus());
		product.setBrand(brandRepo.getReferenceById(dto.getBrandId()));
		product.setThumbnail(fileRepo.getReferenceById(dto.getThumbnailId()));
		
		dto.getProductVariantDtos().stream().forEach(productVariantDto -> {
			ProductVariant productVariant = new ProductVariant();
			
			Set<ProductVariantImage> productVariantImages = new HashSet<>();
			productVariantDto.getImageIds().stream().map(
				fileId -> {
					ProductVariantImage productVariantImage = new ProductVariantImage();
					productVariantImage.setFile(fileRepo.getReferenceById(fileId));
					productVariantImage.setProductVariant(productVariant);
					productVariant.getProductVariantImages().add(productVariantImage);
					return productVariantImage;
				}).collect(Collectors.toSet());
			
			Set<ProductAttribute> productAttributes =
				productVariantDto.getAttributeDtos().stream().map(
					attribute -> {
						ProductAttribute productAttribute = new ProductAttribute();
						productAttribute.setCode(attribute.getCode());
						productAttribute.setValue(attribute.getValue());
						productAttribute.setProductVariant(productVariant);
						productVariant.getProductAttributes().add(productAttribute);
						return productAttribute;
					}).collect(Collectors.toSet());
			
			productVariant.setPrice(productVariantDto.getPrice());
			productVariant.setStock(productVariantDto.getStock());
			productVariant.setProductVariantImages(productVariantImages);
			productVariant.setProductAttributes(productAttributes);
			productVariant.setProduct(product);
			product.getProductVariants().add(productVariant);
		});
		
		productRepo.save(product);
		return new ProductAdminResDto(product, product.getProductVariants());
	}
	
	public ProductAdminResDto updateProductInfo(UpdateProductAdminReqDto dto) {
		Product product = productRepo.findById(dto.getProductId())
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
		
		if (StringUtils.hasText(dto.getName())) product.setName(dto.getName());
		if (StringUtils.hasText(dto.getDescription())) product.setDescription(dto.getDescription());
		if (Objects.nonNull(dto.getBrandId())) product.setBrand(brandRepo.getReferenceById(dto.getBrandId()));
		if (Objects.nonNull(dto.getThumbnailId()))
			product.setThumbnail(fileRepo.getReferenceById(dto.getThumbnailId()));
		if (Objects.nonNull(dto.getStatus())) product.setStatus(dto.getStatus());
		
		return new ProductAdminResDto(product);
	}
	
	public ProductVariantAdminResDto createProductVariant(CreateProductVariantAdminReqDto dto) {
		Product product = productRepo.findById(dto.getProductId())
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
		
		ProductVariant productVariant = new ProductVariant();
		productVariant.setStock(dto.getStock());
		productVariant.setPrice(dto.getPrice());
		productVariant.setProduct(product);
		
		dto.getAttributeDtos().forEach(item -> {
			ProductAttribute productAttribute = new ProductAttribute();
			productAttribute.setCode(item.getCode());
			productAttribute.setValue(item.getValue());
			productAttribute.setProductVariant(productVariant);
			productVariant.getProductAttributes().add(productAttribute);
		});
		
		dto.getImageIds().forEach(item -> {
			ProductVariantImage productVariantImage = new ProductVariantImage();
			productVariantImage.setFile(fileRepo.getReferenceById(item));
			productVariantImage.setProductVariant(productVariant);
			productVariant.getProductVariantImages().add(productVariantImage);
		});
		
		productVariantRepo.save(productVariant);
		return new ProductVariantAdminResDto(productVariant);
	}
	
	public ProductVariantAdminResDto updateProductVariant(UpdateProductVariantAdminReqDto dto) {
		ProductVariant productVariant = productVariantRepo.findById(dto.getProductVariantId())
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product variant not found"));
		productVariant.setStock(dto.getStock());
		productVariant.setPrice(dto.getPrice());
		
		productVariant.getProductAttributes().forEach(item -> {
			UpdateProductVariantAdminReqDto.ProductAttributeDto productAttributeDto = dto.getAttributeDtos().stream()
				.filter(attributeDto -> item.getCode().equals(attributeDto.getCode()))
				.findFirst()
				.orElse(null);
			
			if (Objects.nonNull(productAttributeDto))
				item.setValue(productAttributeDto.getValue());
		});
		
		productVariant.getProductVariantImages()
			.removeIf(item -> !dto.getImageDtos().contains(item.getFile().getId()));
		
		dto.getImageDtos().forEach(item -> {
			boolean isImageExisted = productVariant.getProductVariantImages().stream()
				.anyMatch(productVariantImage -> productVariantImage.getFile().getId().equals(item));
			if (!isImageExisted) {
				ProductVariantImage productVariantImage = new ProductVariantImage();
				productVariantImage.setFile(fileRepo.getReferenceById(item));
				productVariantImage.setProductVariant(productVariant);
				productVariant.getProductVariantImages().add(productVariantImage);
			}
		});
		
		productVariantRepo.save(productVariant);
		
		return new ProductVariantAdminResDto(productVariant);
	}
	
	public void deleteProduct(Long id) {
		Product product = productRepo.findById(id)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
		productRepo.delete(product);
	}
	
	public void deleteProductVariant(Long id) {
		ProductVariant productVariant = productVariantRepo.findById(id)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product variant not found"));
		productVariantRepo.delete(productVariant);
	}
}
