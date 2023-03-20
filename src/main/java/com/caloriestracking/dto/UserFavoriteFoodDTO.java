package com.caloriestracking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserFavoriteFoodDTO {
	private Long id;
	private UserDTO user;
	private FoodDTO food;
}
