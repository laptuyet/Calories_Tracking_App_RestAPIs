package com.caloriestracking.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDTO {
	private Long id;
	private String firstName;
	private String lastName;
	private String image;
	private LocalDate dob;
	private Boolean gender;
	private BigDecimal weight;
	private BigDecimal height;
	private String email;
}
