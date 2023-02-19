package com.caloriestracking.service;

import java.util.List;

import com.caloriestracking.model.UserFavoriteFood;


public interface FavoriteFoodService {
	
	UserFavoriteFood getOneUserFavoriteFood(String username, Long foodId);
	
	List<UserFavoriteFood> getUserFavoriteFoods(String username);

	UserFavoriteFood save(UserFavoriteFood favFood);

	UserFavoriteFood update(UserFavoriteFood favFood);

	void delete(String username);
}
