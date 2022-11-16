package com.example.itshop.repositories;

import com.example.itshop.entities.UserRecovery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRecoveryRepository extends JpaRepository<UserRecovery, Long>, JpaSpecificationExecutor<UserRecovery> {
}