package com.caloriestracking.exceptions;

import java.util.Set;

import lombok.Data;

@Data
public class ObjectNotValidException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	private final Set<String> errorsMessages;

}
