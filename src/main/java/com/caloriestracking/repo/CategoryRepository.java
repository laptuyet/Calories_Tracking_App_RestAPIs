package com.caloriestracking.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.caloriestracking.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long>{
	
	Optional<Category> findByName(String name);
}
