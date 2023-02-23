package com.caloriestracking.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "account")
@Data
@RequiredArgsConstructor
public class Account {
	
	@Id
	@NotEmpty(message = "Username must not be empty")
	@NotBlank(message = "Username must not be blank")
	private String username;
	
	@NotEmpty(message = "Password must not be empty")
	@NotBlank(message = "Password must not be blank")
	@Size(min = 5, max = 30, message = "Password length must be between 5 and 30 characters!")
	private String password;
	
	@Enumerated(EnumType.STRING)
	private Role role;
	
	@OneToOne(mappedBy = "account", cascade = CascadeType.ALL)
	private User user;
}
