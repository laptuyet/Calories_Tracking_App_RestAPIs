package com.caloriestracking.controller;

import java.util.List;
import java.util.stream.Collectors;

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

import com.caloriestracking.dto.FoodDTO;
import com.caloriestracking.mapper.FoodDTOMapper;
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
	
	private final FoodDTOMapper foodDTOMapper;
	
	@GetMapping("{id}")
	public FoodDTO getFood(@PathVariable Long id) {
		return foodDTOMapper.apply(foodService.findById(id));
	}
	
	@GetMapping("/all/category/{cateId}")
	public List<FoodDTO> getAllFoodsByCategory(@PathVariable Long cateId) {
		return foodService.findAllByCategory(cateId)
				.stream()
				.map(foodDTOMapper)
				.collect(Collectors.toList());
	}
	
	@GetMapping("/all/category/{cateId}/paging")
	public List<FoodDTO> getAllFoodsByCategory(
			@PathVariable Long cateId,
			@RequestParam(defaultValue = "0") Integer pageNo,
			@RequestParam(defaultValue = "5") Integer pageSize,
			@RequestParam(defaultValue = "id") String sortBy) {
		return foodService.findAllByCategory(cateId, pageNo, pageSize, sortBy)
				.stream()
				.map(foodDTOMapper)
				.collect(Collectors.toList());
	}
	
	@GetMapping("/all")
	public List<FoodDTO> getAllFoods() {
		return foodService.findAll()
				.stream()
				.map(foodDTOMapper)
				.collect(Collectors.toList());
	}
	
	@GetMapping("/all/paging")
	public List<FoodDTO> getAllFoods(
			@RequestParam(defaultValue = "0") Integer pageNo,
			@RequestParam(defaultValue = "5") Integer pageSize,
			@RequestParam(defaultValue = "id") String sortBy) {
		return foodService.findAll(pageNo, pageSize, sortBy)
				.stream()
				.map(foodDTOMapper)
				.collect(Collectors.toList());
	}
	
	@PostMapping("/create")
	public ResponseEntity<FoodDTO> createFood(
			@RequestPart("food") String foodJson,
			@RequestPart(name = "image", required = false) MultipartFile image) 
					throws JsonMappingException, JsonProcessingException {
		return ResponseEntity.ok(
				foodDTOMapper.apply(
						foodService.save(jsonMapperService.getObjectFromJson(foodJson, Food.class), image)
				)
		);
	}
	
	@PutMapping("/update")
	public ResponseEntity<FoodDTO> updateFood(
			@RequestPart("food") String foodJson,
			@RequestPart(name = "image", required = false) MultipartFile image) 
					throws JsonMappingException, JsonProcessingException {
		return ResponseEntity.ok(
				foodDTOMapper.apply(
						foodService.update(jsonMapperService.getObjectFromJson(foodJson, Food.class), image)
				)
		);
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteFood(@PathVariable Long id) {
		return foodService.delete(id);
	}
	
}
