package com.example.itshop.repositories;

import com.example.itshop.entities.Product;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>,
	JpaSpecificationExecutor<Product> {
	@Query("select p.id from Product p where p.brand.id = :brandId")
	List<Long> findProductIdByBrandId(Long brandId);
	
	@Query("select p from Product p where p.id in :ids")
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@QueryHints()
	List<Product> findProductsByIdsAndLock(List<Long> ids);
}