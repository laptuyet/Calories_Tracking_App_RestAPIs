package com.caloriestracking.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.caloriestracking.model.UserFavoriteFood;


public interface FavoriteFoodService {
	
	UserFavoriteFood getOneUserFavoriteFood(String username, Long foodId);
	
	List<UserFavoriteFood> getUserFavoriteFoods(Long userId);
	
	UserFavoriteFood addToFavoriteList(Long userId, Long foodId);

	ResponseEntity<String> deleteFromFavoriteList(Long userId, Long foodId);
}
