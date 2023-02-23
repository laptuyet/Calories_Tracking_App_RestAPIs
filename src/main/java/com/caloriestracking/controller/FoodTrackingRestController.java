package com.caloriestracking.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.caloriestracking.dto.StatisticResponse;
import com.caloriestracking.dto.UserFoodTrackingDTO;
import com.caloriestracking.mapper.FoodTrackingDTOMapper;
import com.caloriestracking.service.FoodTrackingService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/tracking")
public class FoodTrackingRestController {
	
	private final FoodTrackingService foodTrackingService;
	
	private final FoodTrackingDTOMapper trackingDTOMapper;
	
	@GetMapping("user/{userId}")
	public List<UserFoodTrackingDTO> getUserTrackings(@PathVariable Long userId) {
		return foodTrackingService.getUserTrackings(userId)
				.stream()
				.map(trackingDTOMapper)
				.collect(Collectors.toList());
	}
	
	@PostMapping("add/{userId}/{foodId}/{consumedGram}")
	public ResponseEntity<UserFoodTrackingDTO> addToTrackingList(
			@PathVariable Long userId,
			@PathVariable Long foodId,
			@PathVariable BigDecimal consumedGram) {
		return ResponseEntity.ok(
				trackingDTOMapper.apply(
						foodTrackingService.addToTrackingList(userId, foodId, consumedGram)
				)
		);
	}
	
	@GetMapping("/report")
	public ResponseEntity<StatisticResponse> getReport(
			@RequestParam(value = "userId") Long userId,
			@RequestParam(value = "dateTime", required = false) String dateTime,
			@RequestParam(value = "reportType", required = false,
											defaultValue = "DAY") String reportType
			) {
		return ResponseEntity.ok(
				foodTrackingService.getReport(userId, dateTime, reportType)
		);
	}
}
