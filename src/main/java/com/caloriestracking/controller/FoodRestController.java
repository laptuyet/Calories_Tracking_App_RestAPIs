package com.caloriestracking.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.caloriestracking.model.Food;
import com.caloriestracking.service.FoodService;
import com.caloriestracking.service.JsonMapperService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/foods")
public class FoodRestController {

	private final FoodService foodService;
	
	private final JsonMapperService jsonMapperService;
	
	@GetMapping("{id}")
	public Food getFood(@PathVariable Long id) {
		return foodService.findById(id);
	}
	
	@GetMapping("/all")
	public List<Food> getAllFoods() {
		return foodService.findAll();
	}
	
	@GetMapping("/all/paging")
	public List<Food> getAllFoods(
			@RequestParam(defaultValue = "0") Integer pageNo,
			@RequestParam(defaultValue = "5") Integer pageSize,
			@RequestParam(defaultValue = "id") String sortBy) {
		return foodService.findAll(pageNo, pageSize, sortBy);
	}
	
	@PostMapping("/create")
	public ResponseEntity<Food> createFood(
			@RequestPart("food") String foodJson,
			@RequestPart(name = "image", required = false) MultipartFile image) 
					throws JsonMappingException, JsonProcessingException {
		return ResponseEntity.ok(
				foodService.save(jsonMapperService.getObjectFromJson(foodJson, Food.class), image)
				);
	}
	
	@PutMapping("/update")
	public ResponseEntity<Food> updateFood(
			@RequestPart("food") String foodJson,
			@RequestPart(name = "image", required = false) MultipartFile image) 
					throws JsonMappingException, JsonProcessingException {
		return ResponseEntity.ok(
				foodService.update(jsonMapperService.getObjectFromJson(foodJson, Food.class), image)
				);
	}
	
	@DeleteMapping("/delete/{id}")
	public void deleteFood(@PathVariable Long id) {
		foodService.delete(id);
	}
	
}
