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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/tracking")
@Api("Food Tracking List APIs")
public class FoodTrackingRestController {
	
	private final FoodTrackingService foodTrackingService;
	
	private final FoodTrackingDTOMapper trackingDTOMapper;
	
	@ApiOperation(value = "Lấy tất cả thông tin về Food Tracking của User (userID)")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Lấy thành công",
					response = UserFoodTrackingDTO.class),
			@ApiResponse(code = 400, message = "UserID không hợp lệ")
	})
	@GetMapping("user/{userId}")
	public List<UserFoodTrackingDTO> getUserTrackings(
			@ApiParam(value = "ID của User")
			@PathVariable Long userId) {
		return foodTrackingService.getUserTrackings(userId)
				.stream()
				.map(trackingDTOMapper)
				.collect(Collectors.toList());
	}
	
	@ApiOperation(value = "Thêm một món ăn, lượng gram tiêu thụ vào Tracking List của User")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Thêm thành công",
					response = UserFoodTrackingDTO.class),
			@ApiResponse(code = 400, message = "Thông tin thêm không hợp lệ")
	})
	@PostMapping("add/{userId}/{foodId}/{consumedGram}")
	public ResponseEntity<UserFoodTrackingDTO> addToTrackingList(
			@ApiParam(value = "ID của User")
			@PathVariable Long userId,
			@ApiParam(value = "ID của Food")
			@PathVariable Long foodId,
			@ApiParam(value = "Lượng gram tiêu thụ")
			@PathVariable BigDecimal consumedGram) {
		return ResponseEntity.ok(
				trackingDTOMapper.apply(
						foodTrackingService.addToTrackingList(userId, foodId, consumedGram)
				)
		);
	}
	
	@ApiOperation(value = "Lấy thông tin thống kê lượng Calories tiêu thụ theo tùy chọn thời gian của User")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Lấy thành công",
					response = StatisticResponse.class),
			@ApiResponse(code = 400, message = "Thông tin truyền vào không hợp lệ")
	})
	@GetMapping("/report")
	public ResponseEntity<StatisticResponse> getReport(
			@ApiParam(value = "ID của User")
			@RequestParam(value = "userId") Long userId,
			@ApiParam(value = "yyyy/MM/dd HH:mm:ss thời gian muốn tạo thống kê, kiểu string")
			@RequestParam(value = "dateTime", required = false) String dateTime,
			@ApiParam(value = "Loại thông kê muốn tạo, theo DAY/WEEK/MONTH/YEAR, kiểu string")
			@RequestParam(value = "reportType", required = false,
											defaultValue = "DAY") String reportType
			) {
		return ResponseEntity.ok(
				foodTrackingService.getReport(userId, dateTime, reportType)
		);
	}
}
