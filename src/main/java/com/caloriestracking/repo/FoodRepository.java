package com.caloriestracking.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.caloriestracking.model.Food;

public interface FoodRepository extends JpaRepository<Food, Long>{
	
	Optional<Food> findByName(String name);
	
	List<Food> findAllByCategory_Id(Long cateId);
	
	Page<Food> findAllByCategory_Id(Long cateId, Pageable paging);

}
