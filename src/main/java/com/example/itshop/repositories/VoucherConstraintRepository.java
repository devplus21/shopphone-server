package com.example.itshop.repositories;

import com.example.itshop.entities.VoucherConstraintUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface VoucherConstraintRepository extends JpaRepository<VoucherConstraintUser, Long>,
        JpaSpecificationExecutor<VoucherConstraintUser> {
}