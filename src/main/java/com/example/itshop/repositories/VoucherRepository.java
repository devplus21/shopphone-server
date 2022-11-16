package com.example.itshop.repositories;

import com.example.itshop.entities.Voucher;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.Optional;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Long>,
	JpaSpecificationExecutor<Voucher> {
	@Query("select v from Voucher v where v.id = :id")
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@QueryHints(@QueryHint(name = "javax.persistence.lock.timeout", value = "2000"))
	Optional<Voucher> findByIdAndLock(Long id);
}