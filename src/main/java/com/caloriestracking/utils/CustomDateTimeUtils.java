package com.caloriestracking.utils;

import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;
import java.util.stream.IntStream;

public class CustomDateTimeUtils {

	public static LocalDateTime getFirstDayOfWeek(LocalDateTime dateTime) {
		LocalDateTime firstDayOfWeek = dateTime.with(WeekFields.of(new Locale("vi", "VN")).getFirstDayOfWeek());
		return firstDayOfWeek;
	}
	
	public static List<LocalDateTime> getAllDaysOfTheWeek(LocalDateTime firstDayOfWeek) {
		return IntStream
				.rangeClosed(0, 6)
				.mapToObj(i -> firstDayOfWeek.plusDays(i))
				.toList();
	}
}
