package com.caloriestracking.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.caloriestracking.dto.UserFavoriteFoodDTO;
import com.caloriestracking.mapper.FavoriteFoodDTOMapper;
import com.caloriestracking.service.FavoriteFoodService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/favfoods")
@Api("Favorite Food List APIs")
public class FavoriteFoodsRestController {

	private final FavoriteFoodService favoriteFoodService;
	
	private final FavoriteFoodDTOMapper favoriteFoodDTOMapper;
	
	@ApiOperation(value = "Lấy danh sách đồ ăn yêu thích của User bằng userId")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Lấy danh sách thành công",
					response = List.class),
			@ApiResponse(code = 400, message = "ID của User không hợp lệ"),
			@ApiResponse(code = 404, message = "Không tìm thấy")
	})
	@GetMapping("{userId}")
	public List<UserFavoriteFoodDTO> getAllFavoriteFoodsOfUser(
			@ApiParam(value = "ID của user")
			@PathVariable Long userId) {
		return favoriteFoodService.getUserFavoriteFoods(userId)
				.stream()
				.map(favoriteFoodDTOMapper)
				.collect(Collectors.toList());
	}
	
	@ApiOperation(value = "Thêm 1 Food vào danh sách đồ ăn yêu thích của User")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Thêm vào Favorite List thành công",
					response = UserFavoriteFoodDTO.class),
			@ApiResponse(code = 400, message = "UserId hoặc FoodId không hợp lệ"),
			@ApiResponse(code = 404, message = "Không tìm thấy")
	})
	@PostMapping("add/{userId}/{foodId}")
	public ResponseEntity<UserFavoriteFoodDTO> addToFavoriteList(
			@ApiParam(value = "ID của User")
			@PathVariable Long userId,
			@ApiParam(value = "ID của Food")
			@PathVariable Long foodId) {
		return ResponseEntity.ok(
			favoriteFoodDTOMapper.apply(
				favoriteFoodService.addToFavoriteList(userId, foodId)
			)
		);
				
	}
	
	@ApiOperation(value = "Xóa 1 food khỏi danh sách đồ ăn yêu thích của User")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Xóa Food khỏi Favorite List thành công",
					response = String.class),
			@ApiResponse(code = 400, message = "UserId hoặc FoodId không hợp lệ"),
			@ApiResponse(code = 404, message = "Không tìm thấy")
	})
	@DeleteMapping("delete/{userId}/{foodId}")
	public ResponseEntity<String> deleteFromFavoriteList(
			@ApiParam(value = "ID của User")
			@PathVariable Long userId,
			@ApiParam(value = "ID của Food")
			@PathVariable Long foodId) {
		return favoriteFoodService.deleteFromFavoriteList(userId, foodId);
	}
}
