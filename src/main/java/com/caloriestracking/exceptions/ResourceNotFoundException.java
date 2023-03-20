package com.caloriestracking.exceptions;

import lombok.Data;

@Data
public class ResourceNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private final String errorMessage;
}
