package com.caloriestracking.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.caloriestracking.model.Category;
import com.caloriestracking.service.CategoryService;
import com.caloriestracking.service.JsonMapperService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/categories")
public class CategoryRestController {
	
	private final CategoryService categoryService;
	
	private final JsonMapperService jsonMapperService;
	
	@GetMapping("/{id}")
	public Category getCategory(@PathVariable Long id) {
		return categoryService.findById(id);
	}
	
	@GetMapping("/all")
	public List<Category> getAllCategory() {
		return categoryService.findAll();
	}
	
	@PostMapping("/create")
	public ResponseEntity<Category> createCategory(
			@RequestPart("category") String categoryJson,
			@RequestPart(name = "image", required = false) MultipartFile image)
					throws JsonMappingException, JsonProcessingException {
		
		return ResponseEntity.ok(
				categoryService.save(
						jsonMapperService.getObjectFromJson(categoryJson, Category.class), image)
				);
	}
	
	@PutMapping("/update")
	public ResponseEntity<Category> updateCategory(
			@RequestPart("category") String categoryJson,
			@RequestPart(name = "image", required = false) MultipartFile image)
					throws JsonMappingException, JsonProcessingException {
		
		return ResponseEntity.ok(
				categoryService.update(
						jsonMapperService.getObjectFromJson(categoryJson, Category.class), image)
				);
	}
	
	@DeleteMapping("/delete/{id}")
	public void deleteCategory(@PathVariable Long id) {
		categoryService.delete(id);
	}
}
