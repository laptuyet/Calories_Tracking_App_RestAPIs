package com.caloriestracking.service;

import java.math.BigDecimal;
import java.util.List;

import com.caloriestracking.dto.StatisticResponse;
import com.caloriestracking.model.UserFoodTracking;

public interface FoodTrackingService {
	
	List<UserFoodTracking> getUserTrackings(Long userId);
	
	UserFoodTracking addToTrackingList(Long userId, Long foodId, BigDecimal consumedGram);

	StatisticResponse getReport(Long userId, String dateTime, String reportType);
}
