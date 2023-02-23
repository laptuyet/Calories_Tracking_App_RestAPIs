package com.caloriestracking.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.caloriestracking.dto.AppConstants;
import com.caloriestracking.dto.StatisticResponse;
import com.caloriestracking.exceptions.ResourceNotFoundException;
import com.caloriestracking.model.Food;
import com.caloriestracking.model.User;
import com.caloriestracking.model.UserFoodTracking;
import com.caloriestracking.repo.CustomFoodTrackingRepository;
import com.caloriestracking.repo.UserFoodTrackingRepository;
import com.caloriestracking.service.FoodService;
import com.caloriestracking.service.FoodTrackingService;
import com.caloriestracking.service.UserService;
import com.caloriestracking.utils.CustomDateTimeUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FoodTrackingServiceImpl implements FoodTrackingService {

	private final UserFoodTrackingRepository foodTrackingRepository;

	private final CustomFoodTrackingRepository customeFoodTrackingRepository;

	private final UserService userService;

	private final FoodService foodService;

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
		userFoodTracking.setConsumedDatetime(LocalDateTime.now());

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
			ldt = LocalDateTime.now();
		} else {
			ldt = LocalDateTime.parse(dateTime, formatter);
		}

		switch (reportType.trim().toUpperCase()) {
		case "DAY": {
			timeValues.put("HOUR", ldt.getHour());
			timeValues.put("DAY", ldt.getDayOfMonth());
			timeValues.put("MONTH", ldt.getMonthValue());
			timeValues.put("YEAR", ldt.getYear());
			break;
		}

		case "WEEK": {
			timeValues.put("DAY", ldt.getDayOfMonth());
			timeValues.put("MONTH", ldt.getMonthValue());
			timeValues.put("YEAR", ldt.getYear());
			
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
		
		if (userFoodTrackings.isEmpty())
			throw new ResourceNotFoundException("Datas for this Datetime <" +
					localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "> is empty!");
		
		Map<String, BigDecimal> columndatas = getColumnDatasByTimeOption(userFoodTrackings, localDateTime , reportType);
		
		reportResult.setColumnDatas(columndatas);
		
		reportResult.setMax(userFoodTrackings.stream()
				.max((t1, t2) -> t1.getConsumedGram().compareTo(t2.getConsumedGram())).get().getConsumedGram());

		reportResult.setMin(userFoodTrackings.stream()
				.min((t1, t2) -> t1.getConsumedGram().compareTo(t2.getConsumedGram())).get().getConsumedGram());

		reportResult.setAverage(BigDecimal
				.valueOf(userFoodTrackings.stream().mapToDouble(t -> t.getConsumedGram().doubleValue()).sum()
						/ userFoodTrackings.size()));

		reportResult.setTotal(BigDecimal
				.valueOf(userFoodTrackings.stream().mapToDouble(t -> t.getConsumedGram().doubleValue()).sum()));

		return reportResult;
	}

	private Map<String, BigDecimal> getColumnDatasByTimeOption(List<UserFoodTracking> userFoodTrackings,
			LocalDateTime localDateTime, String reportType) {
		
		Map<String, BigDecimal> columnDatas = new LinkedHashMap<>();
		
		switch (reportType) {
		
		case "DAY": {
			
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
						String.format("%s (%d/%s)", AppConstants.WEEK_COLUMNS[day], dayOfMonth, month >= 10 ? month : "0" + month),
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
	
	private double countCaloriesOfDayInMonth(int day, int month, List<UserFoodTracking> userFoodTrackings) {
		return userFoodTrackings
			.stream()
			.filter(t -> t.getConsumedDatetime().getMonthValue() == month 
						&& t.getConsumedDatetime().getDayOfMonth() == day)
			.mapToDouble(t -> t.getConsumedGram().doubleValue())
			.sum();
	}
	

	private double countCaloriesOfMonth(int month, List<UserFoodTracking> userFoodTrackings) {
		return userFoodTrackings
			.stream()
			.filter(t -> t.getConsumedDatetime().getMonthValue() == month)
			.mapToDouble(t -> t.getConsumedGram().doubleValue())
			.sum();
	}

}
