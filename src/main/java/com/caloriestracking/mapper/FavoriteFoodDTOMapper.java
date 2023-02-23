package com.caloriestracking.mapper;

import java.util.function.Function;

import org.springframework.stereotype.Service;

import com.caloriestracking.dto.UserFavoriteFoodDTO;
import com.caloriestracking.model.UserFavoriteFood;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FavoriteFoodDTOMapper implements Function<UserFavoriteFood, UserFavoriteFoodDTO>{
	
	private final UserDTOMapper userDTOMapper;
	
	private final FoodDTOMapper foodDTOMapper;
	@Override
	public UserFavoriteFoodDTO apply(UserFavoriteFood t) {
		return new UserFavoriteFoodDTO(
				t.getId(),
				userDTOMapper.apply(t.getUser()),
				foodDTOMapper.apply(t.getFood())
		);
	}
	
}
