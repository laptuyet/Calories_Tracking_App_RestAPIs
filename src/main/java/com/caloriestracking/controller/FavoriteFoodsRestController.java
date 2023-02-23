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

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/favfoods")
public class FavoriteFoodsRestController {

	private final FavoriteFoodService favoriteFoodService;
	
	private final FavoriteFoodDTOMapper favoriteFoodDTOMapper;
	
	@GetMapping("{userId}")
	public List<UserFavoriteFoodDTO> getAllFavoriteFoodsOfUser(@PathVariable Long userId) {
		return favoriteFoodService.getUserFavoriteFoods(userId)
				.stream()
				.map(favoriteFoodDTOMapper)
				.collect(Collectors.toList());
	}
	
	@PostMapping("add/{userId}/{foodId}")
	public ResponseEntity<UserFavoriteFoodDTO> addToFavoriteList(
			@PathVariable Long userId,
			@PathVariable Long foodId) {
		return ResponseEntity.ok(
			favoriteFoodDTOMapper.apply(
				favoriteFoodService.addToFavoriteList(userId, foodId)
			)
		);
				
	}
	
	@DeleteMapping("delete/{userId}/{foodId}")
	public ResponseEntity<String> deleteFromFavoriteList(
			@PathVariable Long userId,
			@PathVariable Long foodId) {
		return favoriteFoodService.deleteFromFavoriteList(userId, foodId);
	}
}
