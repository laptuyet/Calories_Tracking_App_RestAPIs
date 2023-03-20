package com.caloriestracking.mapper;

import java.math.BigDecimal;
import java.util.function.Function;

import org.springframework.stereotype.Service;

import com.caloriestracking.dto.FoodTrackingHistory;
import com.caloriestracking.model.UserFoodTracking;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FoodTrackingHistoryDTOMapper implements Function<UserFoodTracking, FoodTrackingHistory>{
	
	private final FoodDTOMapper foodDTOMapper;

	@Override
	public FoodTrackingHistory apply(UserFoodTracking t) {
		return new FoodTrackingHistory(
				foodDTOMapper.apply(t.getFood()),
				t.getConsumedGram(),
				BigDecimal.valueOf(t.getConsumedGram().doubleValue() / 100.0 * t.getFood().getEnergyPerServing().doubleValue()),
				t.getConsumedDatetime()
				);
	}

}
