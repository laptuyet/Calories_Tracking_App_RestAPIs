package com.caloriestracking.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.caloriestracking.dto.StatisticResponse;
import com.caloriestracking.exceptions.ResourceNotFoundException;
import com.caloriestracking.mapper.FoodTrackingHistoryDTOMapper;
import com.caloriestracking.model.Food;
import com.caloriestracking.model.User;
import com.caloriestracking.model.UserFoodTracking;
import com.caloriestracking.repo.CustomFoodTrackingRepository;
import com.caloriestracking.repo.UserFoodTrackingRepository;
import com.caloriestracking.service.FoodService;
import com.caloriestracking.service.FoodTrackingService;
import com.caloriestracking.service.UserService;
import com.caloriestracking.utils.AppConstants;
import com.caloriestracking.utils.CustomDateTimeUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FoodTrackingServiceImpl implements FoodTrackingService {

	private final UserFoodTrackingRepository foodTrackingRepository;

	private final CustomFoodTrackingRepository customeFoodTrackingRepository;

	private final UserService userService;

	private final FoodService foodService;
	
	private final FoodTrackingHistoryDTOMapper historyDTOMapper;

	@Override
	public List<UserFoodTracking> getUserTrackings(Long userId) {
		return foodTrackingRepository.findAllByUser_Id(userId);
	}

	@Override
	public UserFoodTracking addToTrackingList(Long userId, Long foodId, BigDecimal consumedGram) {

		User user = userService.findById(userId);
		Food food = foodService.findById(foodId);

		UserFoodTracking userFoodTracking = new UserFoodTracking();
		userFoodTracking.setUser(user);
		userFoodTracking.setFood(food);
		userFoodTracking.setConsumedGram(consumedGram);
		userFoodTracking.setConsumedDatetime(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));

		return foodTrackingRepository.save(userFoodTracking);
	}

	@Override
	public StatisticResponse getReport(Long userId, String dateTime, String reportType) {

		StatisticResponse reportResult = new StatisticResponse();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime ldt;

		// - key là tên hàm sẽ sử dụng trong query để lấy giá trị về thời gian trong
		// database, Ex: DAY, MONTH, YEAR
		// - value là giá trị thời gian trong locadatetime, để so sánh với giá trị lấy dc
		// từ hàm của key
		Map<String, Integer> timeValues = new HashMap<>();

		if (dateTime == null || dateTime.isEmpty()) { // Nếu không truyền dateTime vào request thì lấy current datetime
			ldt = LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
		} else {
			ldt = LocalDateTime.parse(dateTime, formatter);
		}

		switch (reportType.trim().toUpperCase()) {
		case "DAY": {
			timeValues.put("DAY", ldt.getDayOfMonth());
			timeValues.put("MONTH", ldt.getMonthValue());
			timeValues.put("YEAR", ldt.getYear());
			
			reportResult = createReport(userId, timeValues, ldt, "DAY");
			break;
		}

		case "WEEK": {
			reportResult = createReport(userId, timeValues, ldt, "WEEK");
			break;
		}

		case "MONTH": {
			timeValues.put("MONTH", ldt.getMonthValue());
			timeValues.put("YEAR", ldt.getYear());

			reportResult = createReport(userId, timeValues, ldt, "MONTH");
			break;
		}

		case "YEAR": {
			timeValues.put("YEAR", ldt.getYear());
			reportResult = createReport(userId, timeValues, ldt, "YEAR");
			break;
		}

		default: {
			break;
		}

		} // end switch

		return reportResult;
	}

	private StatisticResponse createReport(Long userId, Map<String, Integer> timeValues,
			LocalDateTime localDateTime,
			String reportType) {
		
		StatisticResponse reportResult = new StatisticResponse();
		
		// get records from database
		List<UserFoodTracking> userFoodTrackings = customeFoodTrackingRepository
				.getTrackingDataWithOption(userId, timeValues, localDateTime, reportType);
		
		// add each food to consumed history
		reportResult.setConsumedHistory(
				userFoodTrackings
					.stream()
					.map(historyDTOMapper)
					.toList()
		);
		
		
		if (userFoodTrackings.isEmpty())
			throw new ResourceNotFoundException("Datas for this Datetime <" +
					localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "> is empty!");
		
		Map<String, BigDecimal> columnData = getColumnDatasByTimeOption(userFoodTrackings, localDateTime , reportType);
		
		// Column Data
		reportResult.setColumnData(columnData);
		
		// Max calories
		reportResult.setMax(BigDecimal.valueOf(columnData
				.values()
				.stream()
				.mapToDouble(t-> t.doubleValue())
				.max()
				.getAsDouble())
		);
		
		// Min calories
		reportResult.setMin(BigDecimal.valueOf(columnData
				.values()
				.stream()
				.mapToDouble(t-> t.doubleValue())
				.min()
				.getAsDouble())
		);
		
		// Average calories
		reportResult.setAverage(BigDecimal.valueOf(columnData
				.values()
				.stream()
				.mapToDouble(t -> t.doubleValue())
				.average()
				.getAsDouble())
		);

		// Total calories
		reportResult.setTotal(BigDecimal.valueOf(columnData
				.values()
				.stream()
				.mapToDouble(t -> t.doubleValue())
				.sum())
		);

		return reportResult;
	}

	private Map<String, BigDecimal> getColumnDatasByTimeOption(List<UserFoodTracking> userFoodTrackings,
			LocalDateTime localDateTime, String reportType) {
		
		Map<String, BigDecimal> columnDatas = new LinkedHashMap<>();
		
		switch (reportType) {
		
		case "DAY": {
			for (int i = 1; i <= 24; i++) {
				columnDatas.put(
						AppConstants.TIME_SPACES[i],
						BigDecimal.valueOf(countCaloriesOfTimeSpacesInDay(i, localDateTime.getHour(), userFoodTrackings))
				);
			}

			break;
		}
		
		case "WEEK": {
			LocalDateTime firstDayOfWeek = CustomDateTimeUtils.getFirstDayOfWeek(localDateTime);
			List<LocalDateTime> allDaysOfTheWeek = CustomDateTimeUtils.getAllDaysOfTheWeek(firstDayOfWeek);
			
			for (int day = 1; day <= 7; day++) {
				LocalDateTime dateInWeek = allDaysOfTheWeek.get(day - 1);
				int dayOfMonth = dateInWeek.getDayOfMonth();
				int month = dateInWeek.getMonthValue();
				
				columnDatas.put(
						String.format("%s (%s/%s)", AppConstants.WEEK_COLUMNS[day], dayOfMonth >= 10 ? dayOfMonth : "0" + dayOfMonth, month >= 10 ? month : "0" + month),
						BigDecimal.valueOf(countCaloriesOfDayInMonth(dateInWeek.getDayOfMonth(),
								dateInWeek.getMonthValue(),
								userFoodTrackings)
						)
				);
			}
			
			break;
		}
		
		case "MONTH": {
			YearMonth yearMonth = YearMonth.of(localDateTime.getYear(), localDateTime.getMonthValue());
			for (int day = 1; day <= yearMonth.lengthOfMonth(); day++) {
				columnDatas.put(String.valueOf(day),
						BigDecimal.valueOf(countCaloriesOfDayInMonth(day, localDateTime.getMonthValue(),  userFoodTrackings)));
			}
			
			break;
		}
		
		case "YEAR": {
			for (int month = 1; month <= 12; month++) {
				columnDatas.put(AppConstants.MONTH_COLUMNS[month],
						BigDecimal.valueOf(countCaloriesOfMonth(month, userFoodTrackings)));
			}
			
			break;
		}
		
		default: {

			break;
		}
		
		} // end switch
		
		return columnDatas;
	}
	
	private double countCaloriesOfTimeSpacesInDay(int upperHourBound, int hour, List<UserFoodTracking> userFoodTrackings) {
		int lowerHourBound = upperHourBound - 1;
		LocalTime lowerBound = LocalTime.of(lowerHourBound, 0, 0);
		LocalTime upperBound = LocalTime.of(upperHourBound - 1, 59, 59);
		return userFoodTrackings
				.stream()
				.filter(t -> (t.getConsumedDatetime().toLocalTime().equals(lowerBound) 
						|| t.getConsumedDatetime().toLocalTime().isAfter(lowerBound)) &&
						 (t.getConsumedDatetime().toLocalTime().equals(upperBound)
						|| t.getConsumedDatetime().toLocalTime().isBefore(upperBound))
				)
				.mapToDouble(t -> (t.getConsumedGram().doubleValue() / 100.0) 
						* (t.getFood().getEnergyPerServing().doubleValue())
				)
				.sum();
	}

	private double countCaloriesOfDayInMonth(int day, int month, List<UserFoodTracking> userFoodTrackings) {
		return userFoodTrackings
			.stream()
			.filter(t -> t.getConsumedDatetime().getMonthValue() == month 
						&& t.getConsumedDatetime().getDayOfMonth() == day)
			.mapToDouble(t -> (t.getConsumedGram().doubleValue() / 100.0) 
					* (t.getFood().getEnergyPerServing().doubleValue())
			)
			.sum();
	}

	private double countCaloriesOfMonth(int month, List<UserFoodTracking> userFoodTrackings) {
		return userFoodTrackings
			.stream()
			.filter(t -> t.getConsumedDatetime().getMonthValue() == month)
			.mapToDouble(t -> (t.getConsumedGram().doubleValue() / 100.0) 
					* (t.getFood().getEnergyPerServing().doubleValue())
			)
			.sum();
	}

	@Override
	public String removeFromTrackingList(Long trackingId) {
		UserFoodTracking userFoodTracking =  foodTrackingRepository.findById(trackingId)
				.orElseThrow(()-> new ResourceNotFoundException("Food tracking item with id<" + trackingId +"> is not found"));
		
		String msg = "";
		
		// Nếu còn trong ngày hiện tại thì cho phép xóa, không thì báo lỗi
		LocalDateTime ldt = LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"));
		if (ldt.toLocalDate().isEqual(userFoodTracking.getConsumedDatetime().toLocalDate())) {
			foodTrackingRepository.delete(userFoodTracking);
			msg = "Delete food tracking item with id<" + trackingId +"> successfully!";
		} else {
			throw new RuntimeException("You only have permission to delete within the day!");
		}
		
		return msg;
	}

}
