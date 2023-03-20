package com.caloriestracking.utils;

public class AppConstants {

	public static final String EMAIL_REGEX_PATTERN = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
	
	public static final String[] TIME_SPACES = {"", "00:00 - 01:00", "01:00 - 02:00", "02:00 - 03:00",
			"03:00 - 04:00", "04:00 - 05:00", "05:00 - 06:00", "06:00 - 07:00", "07:00 - 08:00",
			"08:00 - 09:00", "09:00 - 10:00", "10:00 - 11:00", "11:00 - 12:00", "12:00 - 13:00",
			"13:00 - 14:00", "14:00 - 15:00", "15:00 - 16:00", "16:00 - 17:00", "17:00 - 18:00",
			"18:00 - 19:00", "19:00 - 20:00", "20:00 - 21:00", "21:00 - 22:00", "22:00 - 23:00", "23:00 - 00:00"};
	
	public static final String[] WEEK_COLUMNS = {"", "Mon", "Tue", "Wed", "Thu", "Fri", "Sar", "Sun"};

	public static final String[] MONTH_COLUMNS = {"", "Jan", "Feb", "Mar", "Apr" , "May",
			"Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
	
}
