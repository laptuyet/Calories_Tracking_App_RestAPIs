package com.caloriestracking.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FoodDTO {
	private Long id;
	private String name;
	private String description;
	private String image;
	private BigDecimal protein;
	private BigDecimal fat;
	private BigDecimal carb;
	private BigDecimal energyPerServing;
	private CategoryDTO category;
}
