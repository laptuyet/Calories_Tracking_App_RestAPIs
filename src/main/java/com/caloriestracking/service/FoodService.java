package com.caloriestracking.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.caloriestracking.model.Food;

public interface FoodService {

	Food findById(Long id);
	
	Food findByName(String foodName);

	List<Food> findAll();
	
	List<Food> findAll(Integer pageNo, Integer pageSize, String sortBy);

	Food save(Food food, MultipartFile file);

	Food update(Food food, MultipartFile file);

	ResponseEntity<String> delete(Long id);

	List<Food> findAllByCategory(Long cateId);
	List<Food> findAllByCategory(Long cateId, Integer pageNo, Integer pageSize, String sortBy);
}
