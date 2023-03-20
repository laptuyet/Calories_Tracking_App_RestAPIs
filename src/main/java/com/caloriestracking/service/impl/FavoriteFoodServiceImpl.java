package com.caloriestracking.service.impl;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.caloriestracking.exceptions.ExistingResourceException;
import com.caloriestracking.exceptions.ResourceNotFoundException;
import com.caloriestracking.model.Food;
import com.caloriestracking.model.User;
import com.caloriestracking.model.UserFavoriteFood;
import com.caloriestracking.repo.UserFavoriteFoodRepository;
import com.caloriestracking.service.FavoriteFoodService;
import com.caloriestracking.service.FoodService;
import com.caloriestracking.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FavoriteFoodServiceImpl implements FavoriteFoodService{
	
	private final UserFavoriteFoodRepository favoriteFoodRepository;
	
	private final UserService userService;
	
	private final FoodService foodService;

	@Override
	public UserFavoriteFood getOneUserFavoriteFood(String username, Long foodId) {
		return null;
	}

	@Override
	public List<UserFavoriteFood> getUserFavoriteFoods(Long userId) {
		return favoriteFoodRepository.findFavorListByUser_Id(userId);
	}

	@Override
	public UserFavoriteFood addToFavoriteList(Long userId, Long foodId) {
		
		User user = userService.findById(userId);
		Food food = foodService.findById(foodId);
		
		if (favoriteFoodRepository.findByUser_IdAndFood_Id(userId, foodId) != null) {
			throw new ExistingResourceException("Food with id <" + foodId + "> has already existed"
					+ " in User(id=" + userId + ") favorite list");
		}
		
		UserFavoriteFood userFavFood = new UserFavoriteFood();
		userFavFood.setUser(user);
		userFavFood.setFood(food);
		
		return favoriteFoodRepository.save(userFavFood);
	}

	@Override
	public ResponseEntity<String> deleteFromFavoriteList(Long userId, Long foodId) {
		userService.findById(userId);
		foodService.findById(foodId);
		
		UserFavoriteFood userFavFood = favoriteFoodRepository.findByUser_IdAndFood_Id(userId, foodId);
		
		if (userFavFood == null) {
			throw new ResourceNotFoundException("Food with id <" + foodId + "> is not exist"
					+ " in User(id=" + userId + ") favorite list");
		}
		
		favoriteFoodRepository.delete(userFavFood);
		
		return ResponseEntity.ok("Delete Food(id=" + foodId + ") "
				+ "from User(id=" + userId + ") favorite list successfully!");
	}

}
