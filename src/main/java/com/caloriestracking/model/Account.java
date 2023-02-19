package com.caloriestracking.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

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
	
	private String password;
	
	@Enumerated(EnumType.STRING)
	private Role role;
	
	@OneToOne(mappedBy = "account")
	private User user;
}
