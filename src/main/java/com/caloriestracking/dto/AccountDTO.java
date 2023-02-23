package com.caloriestracking.dto;

import com.caloriestracking.model.Role;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AccountDTO {
	private String username;
	private Role role;
	private UserDTO user;
}
