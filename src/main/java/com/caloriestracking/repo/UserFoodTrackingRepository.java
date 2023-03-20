package com.caloriestracking.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.caloriestracking.model.UserFoodTracking;

public interface UserFoodTrackingRepository extends JpaRepository<UserFoodTracking, Long>{
	
	List<UserFoodTracking> findFoodTrackingByUser_Id(Long userId);
	
	List<UserFoodTracking> findFoodTrackingByFood_Id(Long foodId);
	
	List<UserFoodTracking> findAllByUser_Id(Long userId);
	
	UserFoodTracking findByUser_IdAndFood_Id(Long userId, Long foodId);
}
