package com.example.itshop.repositories;

import com.example.itshop.entities.Review;
import com.example.itshop.enums.ReviewStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long>,
	JpaSpecificationExecutor<Review> {
	Optional<Review> findFirstByOrder_User_IdAndStatus(Long userId, ReviewStatus status);
}