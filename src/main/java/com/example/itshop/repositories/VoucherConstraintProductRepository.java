package com.example.itshop.repositories;

import com.example.itshop.entities.VoucherConstraintProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface VoucherConstraintProductRepository extends JpaRepository<VoucherConstraintProduct, Long>,
	JpaSpecificationExecutor<VoucherConstraintProduct> {
}