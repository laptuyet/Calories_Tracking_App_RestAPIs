package com.caloriestracking.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserFoodTrackingDTO {
	
	private Long id;
	
	private UserDTO user;
	
	private FoodDTO food;
	
	private BigDecimal consumedGram;
	
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime consumedDatetime;
}
