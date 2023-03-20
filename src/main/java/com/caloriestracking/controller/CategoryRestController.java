package com.caloriestracking.controller;

import java.util.List;

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

import com.caloriestracking.model.Category;
import com.caloriestracking.service.CategoryService;
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
@RequestMapping("api/v1/categories")
@Api(value = "Category APIs")
public class CategoryRestController {
	
	private final CategoryService categoryService;
	
	private final JsonMapperService jsonMapperService;
	
	@ApiOperation(value = "Tìm một Category bằng ID")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Lấy thông tin Category thành công",
					response = Category.class),
			@ApiResponse(code = 404, message = "Không tìm Category")
	})
	@GetMapping("/{id}")
	public Category getCategory(
			@ApiParam(value = "ID của Category cần tìm")
			@PathVariable Long id) {
		return categoryService.findById(id);
	}
	
	@ApiOperation(value = "Xem danh sách có phân trang các Category ", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Lấy danh sách các Category thành công",
					response = Category.class,
					responseContainer = "List"),
			@ApiResponse(code = 404, message = "Không tìm danh sách các Category")
	})
	@GetMapping("/all/paging")
	public List<Category> getAllCategory(
			@ApiParam(value = "Page number", defaultValue = "0")
			@RequestParam(defaultValue = "0") Integer pageNo,
			@ApiParam(value = "Page size", defaultValue = "5")
			@RequestParam(defaultValue = "5") Integer pageSize,
			@ApiParam(value = "Sort type", defaultValue = "Sort by 'id'")
			@RequestParam(defaultValue = "id") String sortBy) {
		return categoryService.findAll(pageNo, pageSize, sortBy);
	}
	
	@ApiOperation(value = "Xem danh sách các Category ", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Lấy danh sách các Category thành công",
					response = Category.class,
					responseContainer = "List"),
			@ApiResponse(code = 404, message = "Không tìm danh sách các Category")
	})
	@GetMapping("/all")
	public List<Category> getAllCategory() {
		return categoryService.findAll();
	}
	
	@ApiOperation(value = "Tạo mới một Category")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Tạo mới một Category thành công",
					response = Category.class),
			@ApiResponse(code = 400, message = "Thông tin bị thiếu hoặc không hợp lệ")
	})
	@PostMapping("/create")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Category> createCategory(
			@ApiParam(value = "Json category")
			@RequestPart("category") String categoryJson,
			@ApiParam(value = "Image của Category", required = false)
			@RequestPart(name = "image", required = false) MultipartFile image)
					throws JsonMappingException, JsonProcessingException {
		
		return ResponseEntity.ok(
				categoryService.save(
						jsonMapperService.getObjectFromJson(categoryJson, Category.class), image)
				);
	}
	
	@ApiOperation(value = "Cập nhật Category")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Cập nhật Category thành công",
					response = Category.class),
			@ApiResponse(code = 400, message = "Thông tin bị thiếu hoặc không hợp lệ")
	})
	@PutMapping("/update")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Category> updateCategory(
			@ApiParam(value = "Json category")
			@RequestPart("category") String categoryJson,
			@ApiParam(value = "Image của Category", required = false)
			@RequestPart(name = "image", required = false) MultipartFile image)
					throws JsonMappingException, JsonProcessingException {
		
		return ResponseEntity.ok(
				categoryService.update(
						jsonMapperService.getObjectFromJson(categoryJson, Category.class), image)
				);
	}
	
	@ApiOperation(value = "Xóa một Category")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Xóa Category thành công",
					response = Category.class),
			@ApiResponse(code = 400, message = "ID của không hợp lệ"),
			@ApiResponse(code = 404, message = "Không tìm thấy Category")
	})
	@DeleteMapping("/delete/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<String> deleteCategory(
			@ApiParam(value = "ID của Category cần xóa")
			@PathVariable Long id) {
		return categoryService.delete(id);
	}
}
