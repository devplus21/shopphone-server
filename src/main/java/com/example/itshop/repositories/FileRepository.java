package com.example.itshop.repositories;

import com.example.itshop.entities.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface FileRepository extends JpaRepository<File, Long>,
	JpaSpecificationExecutor<File> {
}