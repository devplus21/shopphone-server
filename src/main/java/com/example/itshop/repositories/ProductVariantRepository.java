package com.example.itshop.repositories;

import com.example.itshop.entities.ProductVariant;
import com.example.itshop.enums.ProductStatus;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.List;

@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long>,
	JpaSpecificationExecutor<ProductVariant> {
	
	@Query("select pv from ProductVariant pv where pv.id in :ids")
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value = "3000")})
	List<ProductVariant> findByIdsAndLock(List<Long> ids);
}