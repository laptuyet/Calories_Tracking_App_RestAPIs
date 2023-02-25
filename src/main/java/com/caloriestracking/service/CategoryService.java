package com.caloriestracking.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.caloriestracking.model.Category;

public interface CategoryService {
	
	Category findById(Long id);
	
	Category findByCategoryName(String categoryName);

	List<Category> findAll();
	
	List<Category> findAll(Integer pageNo, Integer pageSize, String sortBy);

	Category save(Category category, MultipartFile file);

	Category update(Category category, MultipartFile file);

	void delete(String categoryName);
	
	ResponseEntity<String> delete(Long id);

}
