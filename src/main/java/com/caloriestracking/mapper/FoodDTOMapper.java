package com.caloriestracking.mapper;

import java.util.function.Function;

import org.springframework.stereotype.Service;

import com.caloriestracking.dto.FoodDTO;
import com.caloriestracking.model.Food;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FoodDTOMapper implements Function<Food, FoodDTO>{
	
	private final CategoryDTOMapper categoryDTOMapper;

	@Override
	public FoodDTO apply(Food t) {
		return new FoodDTO(
				t.getId(),
				t.getName(),
				t.getDescription(),
				t.getImage(),
				t.getProtein(),
				t.getFat(),
				t.getCarb(),
				t.getEnergyPerServing(),
				categoryDTOMapper.apply(t.getCategory())
		);
	}
	
}
