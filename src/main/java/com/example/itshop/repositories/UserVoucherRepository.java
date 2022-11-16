package com.example.itshop.repositories;

import com.example.itshop.entities.User;
import com.example.itshop.entities.UserVoucher;
import com.example.itshop.entities.Voucher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserVoucherRepository extends JpaRepository<UserVoucher, Long>,
	JpaSpecificationExecutor<UserVoucher> {
	Page<UserVoucher> findByUser(User user, Pageable pageable);
	boolean existsByUserAndVoucher(User user, Voucher voucher);
	
	@Query("select uv from UserVoucher uv where uv.id in :ids")
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value = "3000")})
	List<UserVoucher> findByIdsAndLock(List<Long> ids);
	
	@Query("select uv from UserVoucher uv where uv.id in :id")
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value = "3000")})
	Optional<UserVoucher> findByIdAndLock(Long id);
}