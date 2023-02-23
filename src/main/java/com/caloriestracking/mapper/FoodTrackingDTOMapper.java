package com.caloriestracking.mapper;

import java.util.function.Function;

import org.springframework.stereotype.Service;

import com.caloriestracking.dto.UserFoodTrackingDTO;
import com.caloriestracking.model.UserFoodTracking;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FoodTrackingDTOMapper implements Function<UserFoodTracking, UserFoodTrackingDTO> {
	
	private final UserDTOMapper userDTOMapper;
	
	private final FoodDTOMapper foodDTOMapper;
	
	@Override
	public UserFoodTrackingDTO apply(UserFoodTracking t) {
		return new UserFoodTrackingDTO(
				t.getId(),
				userDTOMapper.apply(t.getUser()),
				foodDTOMapper.apply(t.getFood()),
				t.getConsumedGram(),
				t.getConsumedDatetime()
		);
	}

}
