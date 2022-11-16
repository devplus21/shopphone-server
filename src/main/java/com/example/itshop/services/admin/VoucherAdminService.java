package com.example.itshop.services.admin;

import com.example.itshop.dto.admin.request.CreateVoucherAdminReqDto;
import com.example.itshop.dto.admin.request.UpdateVoucherAdminReqDto;
import com.example.itshop.dto.admin.request.VoucherSearchAdminReqDto;
import com.example.itshop.dto.admin.response.VoucherAdminResDto;
import com.example.itshop.dto.common.PaginationResponseDto;
import com.example.itshop.entities.*;
import com.example.itshop.repositories.ProductRepository;
import com.example.itshop.repositories.VoucherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.criteria.Predicate;
import java.time.OffsetDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class VoucherAdminService {
	private final VoucherRepository voucherRepo;
	private final ProductRepository productRepo;
	
	public PaginationResponseDto<VoucherAdminResDto> getAll(VoucherSearchAdminReqDto dto) {
		Page<Voucher> voucherPage = voucherRepo.findAll((root, query, builder) -> {
			List<Predicate> predicates = new ArrayList<>();
			if (StringUtils.hasText(dto.getSearchText())) {
				predicates.add(
					builder.equal(root.get(Voucher_.NAME), "%" + dto.getSearchText() + "%"));
			}
			if (StringUtils.hasText(dto.getStartsAt())) {
				predicates.add(
					builder.greaterThanOrEqualTo(root.get(Voucher_.STARTS_AT),
						OffsetDateTime.parse(dto.getStartsAt()))
				);
			}
			if (StringUtils.hasText(dto.getExpiresAt())) {
				predicates.add(
					builder.lessThanOrEqualTo(root.get(Voucher_.STARTS_AT),
						OffsetDateTime.parse(dto.getExpiresAt()))
				);
			}
			if (Objects.nonNull(dto.getStatus())) {
				predicates.add(
					builder.equal(root.get(Voucher_.STATUS), dto.getStatus())
				);
			}
			if (Objects.nonNull(dto.getType())) {
				predicates.add(
					builder.equal(root.get(Voucher_.TYPE), dto.getType())
				);
			}
			return query.where(predicates.toArray(new Predicate[0])).getRestriction();
		}, dto.getPageable());
		
		List<VoucherAdminResDto> adminResDtos =
			voucherPage.map(VoucherAdminResDto::new).stream().toList();
		return new PaginationResponseDto<>(adminResDtos, voucherPage);
	}
	
	public VoucherAdminResDto create(CreateVoucherAdminReqDto dto) {
		Voucher voucher = new Voucher();
		this.setVoucherAttribute(voucher, dto);
		voucher.setQuantity(dto.getQuantity());
		
		VoucherConstraintUser voucherConstraintUser = new VoucherConstraintUser();
		voucherConstraintUser.setConstraintType(dto.getConstraintUserType());
		
		VoucherConstraintProduct voucherConstraintProduct = new VoucherConstraintProduct();
		voucherConstraintProduct.setConstraintType(dto.getConstraintProductType());
		
		this.addConstraintProduct(voucherConstraintProduct, dto);
		
		voucher.setVoucherConstraintProduct(voucherConstraintProduct);
		voucher.setVoucherConstraintUser(voucherConstraintUser);
		voucherConstraintUser.setVoucher(voucher);
		voucherConstraintProduct.setVoucher(voucher);
		
		voucherRepo.save(voucher);
		return new VoucherAdminResDto(voucher);
	}
	
	public VoucherAdminResDto update(UpdateVoucherAdminReqDto dto) {
		Voucher voucher = voucherRepo.findById(dto.getId())
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Voucher not found0"));
		this.setVoucherAttribute(voucher, dto);
		voucher.setQuantity(voucher.getQuantity() + dto.getQuantity());
		
		voucher.getVoucherConstraintUser().setConstraintType(dto.getConstraintUserType());
		voucher.getVoucherConstraintProduct().setConstraintType(dto.getConstraintProductType());
		
		this.addConstraintProduct(voucher.getVoucherConstraintProduct(), dto);
		
		voucherRepo.save(voucher);
		return new VoucherAdminResDto(voucher);
	}
	
	public VoucherAdminResDto getDetail(Long id) {
		Voucher voucher = voucherRepo.findById(id)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Voucher not found"));
		return new VoucherAdminResDto(voucher);
	}
	
	public void deleteVoucher(Long id) {
		Voucher voucher = voucherRepo.findById(id)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Voucher not found"));
		voucherRepo.delete(voucher);
	}
	
	private void setVoucherAttribute(Voucher voucher, CreateVoucherAdminReqDto dto) {
		voucher.setName(dto.getName());
		voucher.setDiscountAmount(dto.getDiscountAmount());
		voucher.setUnit(dto.getUnit());
		voucher.setStartsAt(OffsetDateTime.parse(dto.getStartsAt()));
		voucher.setExpiresAt(OffsetDateTime.parse(dto.getExpiresAt()));
		voucher.setType(dto.getVoucherType());
	}
	
	private void addConstraintProduct(VoucherConstraintProduct voucherConstraintProduct, CreateVoucherAdminReqDto dto) {
		switch (dto.getConstraintProductType()) {
			case BRAND -> {
				List<Long> productIds = productRepo.findProductIdByBrandId(dto.getBrandId());
				
				voucherConstraintProduct.getVoucherConstraintProductDetails()
					.removeIf(detail -> !productIds.contains(detail.getProduct().getId()));
				
				productIds.stream()
					.map(Product::new)
					.forEach(item -> {
						VoucherConstraintProductDetail detail = new VoucherConstraintProductDetail();
						detail.setProduct(item);
						detail.setVoucherConstraintProduct(voucherConstraintProduct);
						voucherConstraintProduct.getVoucherConstraintProductDetails().add(detail);
					});
			}
			case CUSTOM -> {
				voucherConstraintProduct.getVoucherConstraintProductDetails()
					.removeIf(detail -> !dto.getProductIds().contains(detail.getProduct().getId()));
				
				dto.getProductIds().stream()
					.map(Product::new)
					.forEach(item -> {
						VoucherConstraintProductDetail detail = new VoucherConstraintProductDetail();
						detail.setProduct(item);
						detail.setVoucherConstraintProduct(voucherConstraintProduct);
						voucherConstraintProduct.getVoucherConstraintProductDetails().add(detail);
					});
			}
		}
	}
}
