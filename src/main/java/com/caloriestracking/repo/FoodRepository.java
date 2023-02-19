package com.caloriestracking.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.caloriestracking.model.Food;

public interface FoodRepository extends JpaRepository<Food, Long>{
	
	Optional<Food> findByName(String name);

}
