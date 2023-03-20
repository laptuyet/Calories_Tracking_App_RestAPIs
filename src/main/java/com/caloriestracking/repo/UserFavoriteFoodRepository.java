package com.caloriestracking.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.caloriestracking.model.UserFavoriteFood;

public interface UserFavoriteFoodRepository extends JpaRepository<UserFavoriteFood, Long>{
	
	List<UserFavoriteFood> findFavorListByFood_Id(Long foodId);
	
	List<UserFavoriteFood> findFavorListByUser_Id(Long userId);
	
	UserFavoriteFood findByUser_IdAndFood_Id(Long userId, Long foodId);
}
