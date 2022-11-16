package com.example.itshop.services.admin;

import com.example.itshop.dto.admin.request.AccountSearchAdminReqDto;
import com.example.itshop.dto.admin.request.CreateAccountAdminReqDto;
import com.example.itshop.dto.admin.request.LoginAdminReqDto;
import com.example.itshop.dto.admin.request.UpdateAccountAdminReqDto;
import com.example.itshop.dto.admin.response.AccountAdminResDto;
import com.example.itshop.dto.admin.response.LoginAdminResDto;
import com.example.itshop.dto.common.PaginationResponseDto;
import com.example.itshop.entities.Admin;
import com.example.itshop.entities.Admin_;
import com.example.itshop.enums.ClientType;
import com.example.itshop.repositories.AdminRepository;
import com.example.itshop.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminAccountService {
	private final AdminRepository adminRepo;
	private final PasswordEncoder passwordEncoder;
	private final SecurityUtil securityUtil;
	
	public PaginationResponseDto<AccountAdminResDto> getAll(AccountSearchAdminReqDto dto) {
		Page<Admin> adminPage = adminRepo.findAll((root, query, builder) -> {
			List<Predicate> predicates = new ArrayList<>();
			if (StringUtils.hasText(dto.getSearchText())) {
				List<Predicate> predicatesSearchText = new ArrayList<>();
				String searchText = "%" + dto.getSearchText() + "%";
				predicatesSearchText.add(builder.equal(root.get(Admin_.EMAIL), searchText));
				predicatesSearchText.add(builder.equal(root.get(Admin_.NAME), searchText));
				predicates.add(builder.or(predicatesSearchText.toArray(new Predicate[0])));
			}
			return query.where(predicates.toArray(new Predicate[0])).getRestriction();
		}, dto.getPageable());
		
		List<AccountAdminResDto> resDtos = adminPage.map(AccountAdminResDto::new).stream().toList();
		return new PaginationResponseDto<>(resDtos, adminPage);
	}
	
	public AccountAdminResDto create(CreateAccountAdminReqDto dto) {
		boolean isExisted = adminRepo.existsByEmail(dto.getEmail());
		if (isExisted) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Admin already exists");
		}
		Admin admin = new Admin();
		admin.setEmail(dto.getEmail());
		admin.setPassword(passwordEncoder.encode(dto.getPassword()));
		adminRepo.save(admin);
		return new AccountAdminResDto(admin);
	}
	
	public AccountAdminResDto update(UpdateAccountAdminReqDto dto) {
		Admin admin = adminRepo.findById(dto.getId())
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Admin not found"));
		if (StringUtils.hasText(dto.getName())) {
			admin.setName(dto.getName());
		}
		if (StringUtils.hasText(dto.getNewPassword())) {
			if (!passwordEncoder.matches(dto.getCurrentPassword(), admin.getPassword())) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Wrong password");
			}
			admin.setPassword(passwordEncoder.encode(dto.getNewPassword()));
		}
		adminRepo.save(admin);
		return new AccountAdminResDto(admin);
	}
	
	public AccountAdminResDto getDetail(Long id) {
		Admin admin = adminRepo.findById(id)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Admin not found"));
		return new AccountAdminResDto(admin);
	}
	
	public LoginAdminResDto login(LoginAdminReqDto dto) {
		Admin admin = adminRepo.findByEmail(dto.getEmail())
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Admin not found"));
		
		if (!passwordEncoder.matches(dto.getPassword(), admin.getPassword())) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Wrong password");
		}
		
		String accessToken = securityUtil.createAccessToken(admin.getId(), ClientType.ADMIN);
		return new LoginAdminResDto(admin, accessToken, null);
	}
	
	public void deleteAdmin(Long id) {
		Admin admin = adminRepo.findById(id)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Admin not found"));
		adminRepo.delete(admin);
	}
	
	public AccountAdminResDto getCurrent() {
		return new AccountAdminResDto(SecurityUtil.getCurrentAdmin());
	}
}
