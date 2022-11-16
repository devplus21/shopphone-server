package com.example.itshop.repositories;

import com.example.itshop.entities.Cart;
import com.example.itshop.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long>, JpaSpecificationExecutor<Cart> {
	List<Cart> findByUser(User user);
	Integer deleteByIdAndUser(Long id, User user);
}