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

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "account")
@Data
@RequiredArgsConstructor
@ApiModel(value = "Account model")
public class Account {
	
	@ApiModelProperty(position = 1, value = "Username của Account")
	@Id
	@NotEmpty(message = "Username must not be empty")
	@NotBlank(message = "Username must not be blank")
	private String username;
	
	@ApiModelProperty(position = 2, value = "Password của Account")
	@NotEmpty(message = "Password must not be empty")
	@NotBlank(message = "Password must not be blank")
	@Size(min = 5, message = "Password length must be between 5 and 30 characters!")
	private String password;
	
	@ApiModelProperty(position = 3, value = "Role của Account, mặc định là ROLE_USER")
	@Enumerated(EnumType.STRING)
	private Role role;
	
	@ApiModelProperty(position = 4, value = "Đối tượng User liên kết với Account")
	@OneToOne(mappedBy = "account", cascade = CascadeType.ALL)
	private User user;
}
