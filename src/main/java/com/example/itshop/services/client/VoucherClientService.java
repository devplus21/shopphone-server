package com.example.itshop.services.client;

import com.example.itshop.dto.common.PaginationRequestDto;
import com.example.itshop.dto.client.request.VoucherSearchClientReqDto;
import com.example.itshop.dto.common.PaginationResponseDto;
import com.example.itshop.dto.client.response.UserVoucherClientResDto;
import com.example.itshop.dto.client.response.VoucherClientResDto;
import com.example.itshop.entities.*;
import com.example.itshop.enums.VoucherStatus;
import com.example.itshop.repositories.OrderRepository;
import com.example.itshop.repositories.UserVoucherRepository;
import com.example.itshop.repositories.VoucherConstraintRepository;
import com.example.itshop.repositories.VoucherRepository;
import com.example.itshop.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.criteria.Predicate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@Service
@RequiredArgsConstructor
@Transactional
public class VoucherClientService {
	private final VoucherConstraintRepository voucherConstraintRepo;
	private final VoucherRepository voucherRepo;
	private final UserVoucherRepository userVoucherRepo;
	private final OrderRepository orderRepo;
	
	
	public PaginationResponseDto<VoucherClientResDto> getAll(VoucherSearchClientReqDto dto) {
		Page<Voucher> voucherPage = voucherRepo.findAll((root, query, builder) -> {
			List<Predicate> predicates = new ArrayList<>();
			if (Objects.nonNull(dto.getVoucherType())) {
				predicates.add(builder.equal(root.get(Voucher_.TYPE), dto.getVoucherType()));
			}
			predicates.add(builder.lessThanOrEqualTo(
				root.get(Voucher_.STARTS_AT), OffsetDateTime.now()
			));
			predicates.add(builder.greaterThanOrEqualTo(
				root.get(Voucher_.EXPIRES_AT), OffsetDateTime.now()
			));
			predicates.add(builder.equal(root.get(Voucher_.STATUS), VoucherStatus.ACTIVE));
			return query.where(predicates.toArray(new Predicate[0])).getRestriction();
		}, dto.getPageable());
		
		List<VoucherClientResDto> vouchers = voucherPage.map(VoucherClientResDto::new).stream().toList();
		
		return new PaginationResponseDto<>(vouchers, voucherPage);
	}
	
	public PaginationResponseDto<UserVoucherClientResDto> getMyVoucher(PaginationRequestDto dto) {
		User user = SecurityUtil.getCurrentUser();
		Page<UserVoucher> userVoucherPage = userVoucherRepo.findByUser(user, dto.getPageable());
		List<UserVoucherClientResDto> responseDtos = userVoucherPage
			.map(UserVoucherClientResDto::new)
			.stream().toList();
		
		return new PaginationResponseDto<>(responseDtos, userVoucherPage);
	}
	
	public UserVoucherClientResDto claimVoucher(Long voucherId) {
		User user = SecurityUtil.getCurrentUser();
		
		Voucher voucher = voucherRepo.findByIdAndLock(voucherId)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Voucher not found"));
		
		if (userVoucherRepo.existsByUserAndVoucher(user, voucher)) {
			throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "Voucher has already claimed");
		}
		if (!voucher.getStatus().equals(VoucherStatus.ACTIVE)) {
			throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "Voucher is in active");
		}
		if (OffsetDateTime.now().isBefore(voucher.getStartsAt())) {
			throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "Voucher is not ready to claim");
		}
		if (OffsetDateTime.now().isAfter(voucher.getExpiresAt())) {
			throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "Voucher expires");
		}
		if (voucher.getQuantity() <= voucher.getUsedAmount()) {
			throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "Out of voucher");
		}
		
		switch (voucher.getVoucherConstraintUser().getConstraintType()) {
			case NEW_USER -> {
				if (orderRepo.existsByUser(user)) {
					throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED,
						"You can not claim this voucher");
				}
			}
		}
		
		voucher.setUsedAmount(voucher.getUsedAmount() + 1);
		if (voucher.getQuantity().equals(voucher.getUsedAmount())) {
			voucher.setStatus(VoucherStatus.IN_ACTIVE);
		}
		
		UserVoucher userVoucher = new UserVoucher();
		userVoucher.setUser(user);
		userVoucher.setVoucher(voucher);
		
		userVoucherRepo.save(userVoucher);
		voucherRepo.save(voucher);
		
		return new UserVoucherClientResDto(userVoucher);
	}
	
}
