package com.caloriestracking.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/foods")
@Api("Food APIs")
public class FoodRestController {

	private final FoodService foodService;
	
	private final JsonMapperService jsonMapperService;
	
	private final FoodDTOMapper foodDTOMapper;
	
	@ApiOperation(value = "Lấy thông tin của 1 Food")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Lấy thông tin thành công",
					response = FoodDTO.class),
			@ApiResponse(code = 400, message = "FoodId không hợp lệ"),
			@ApiResponse(code = 404, message = "Không tìm thấy")
	})
	@GetMapping("{id}")
	public FoodDTO getFood(
			@ApiParam(value = "ID của Food")
			@PathVariable Long id) {
		return foodDTOMapper.apply(foodService.findById(id));
	}
	
	@ApiOperation(value = "Lấy danh sách các Food theo danh mục (categoryId)")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Lấy thành công",
					response = List.class),
			@ApiResponse(code = 400, message = "CategoryID không hợp lệ"),
			@ApiResponse(code = 404, message = "Không tìm thấy")
	})
	@GetMapping("/all/category/{cateId}")
	public List<FoodDTO> getAllFoodsByCategory(
			@ApiParam(value = "ID của Category")
			@PathVariable Long cateId) {
		return foodService.findAllByCategory(cateId)
				.stream()
				.map(foodDTOMapper)
				.collect(Collectors.toList());
	}
	
	@ApiOperation(value = "Lấy danh sách các Food theo danh mục (categoryId) có phân trang")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Lấy thành công",
					response = List.class),
			@ApiResponse(code = 400, message = "CategoryID không hợp lệ"),
			@ApiResponse(code = 404, message = "Không tìm thấy")
	})
	@GetMapping("/all/category/{cateId}/paging")
	public List<FoodDTO> getAllFoodsByCategory(
			@ApiParam(value = "ID của Caetgory")
			@PathVariable Long cateId,
			@ApiParam(value = "Page number", defaultValue = "0")
			@RequestParam(defaultValue = "0") Integer pageNo,
			@ApiParam(value = "Page size", defaultValue = "5")
			@RequestParam(defaultValue = "5") Integer pageSize,
			@ApiParam(value = "Sort type", defaultValue = "Sort by 'id'")
			@RequestParam(defaultValue = "id") String sortBy) {
		return foodService.findAllByCategory(cateId, pageNo, pageSize, sortBy)
				.stream()
				.map(foodDTOMapper)
				.collect(Collectors.toList());
	}
	
	@ApiOperation(value = "Lấy danh sách tất cả các Food")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Lấy thành công",
					response = List.class),
			@ApiResponse(code = 404, message = "Không tìm thấy")
	})
	@GetMapping("/all")
	public List<FoodDTO> getAllFoods() {
		return foodService.findAll()
				.stream()
				.map(foodDTOMapper)
				.collect(Collectors.toList());
	}
	
	@ApiOperation(value = "Lấy danh sách tất cả các Food có phân trang")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Lấy thành công",
					response = List.class),
			@ApiResponse(code = 404, message = "Không tìm thấy")
	})
	@GetMapping("/all/paging")
	public List<FoodDTO> getAllFoods(
			@ApiParam(value = "Page number", defaultValue = "0")
			@RequestParam(defaultValue = "0") Integer pageNo,
			@ApiParam(value = "Page size", defaultValue = "5")
			@RequestParam(defaultValue = "5") Integer pageSize,
			@ApiParam(value = "Sort type", defaultValue = "Sort by 'id'")
			@RequestParam(defaultValue = "id") String sortBy) {
		return foodService.findAll(pageNo, pageSize, sortBy)
				.stream()
				.map(foodDTOMapper)
				.collect(Collectors.toList());
	}
	
	@ApiOperation(value = "Tạo mới Food")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Tạo thành công",
					response = FoodDTO.class),
			@ApiResponse(code = 400, message = "Thông tin tạo mới không hợp lệ")
	})
	@PostMapping("/create")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<FoodDTO> createFood(
			@ApiParam(value = "String Food JSON")
			@RequestPart("food") String foodJson,
			@ApiParam(value = "Image của Food", required = false)
			@RequestPart(name = "image", required = false) MultipartFile image) 
					throws JsonMappingException, JsonProcessingException {
		return ResponseEntity.ok(
				foodDTOMapper.apply(
						foodService.save(jsonMapperService.getObjectFromJson(foodJson, Food.class), image)
				)
		);
	}
	
	@ApiOperation(value = "Update Food")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Update thành công",
					response = FoodDTO.class),
			@ApiResponse(code = 400, message = "Thông tin Update không hợp lệ")
	})
	@PutMapping("/update")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<FoodDTO> updateFood(
			@ApiParam(value = "String Food JSON")
			@RequestPart("food") String foodJson,
			@ApiParam(value = "Image của Food", required = false)
			@RequestPart(name = "image", required = false) MultipartFile image) 
					throws JsonMappingException, JsonProcessingException {
		return ResponseEntity.ok(
				foodDTOMapper.apply(
						foodService.update(jsonMapperService.getObjectFromJson(foodJson, Food.class), image)
				)
		);
	}
	
	@ApiOperation(value = "Xóa Food")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Xóa thành công",
					response = String.class),
			@ApiResponse(code = 404, message = "Không tìm thấy Food ID")
	})
	@DeleteMapping("/delete/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> deleteFood(
			@ApiParam(value = "ID của Food cần xóa")
			@PathVariable Long id) {
		return foodService.delete(id);
	}
	
}
