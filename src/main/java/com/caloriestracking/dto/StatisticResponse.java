package com.caloriestracking.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatisticResponse {
	
	private Map<String, BigDecimal> columnData;
	
	private BigDecimal max;
	
	private BigDecimal min;
	
	private BigDecimal average;
	
	private BigDecimal total;
	
	private List<FoodTrackingHistory> consumedHistory;
}
