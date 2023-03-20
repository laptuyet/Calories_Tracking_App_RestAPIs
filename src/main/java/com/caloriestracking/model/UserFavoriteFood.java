package com.caloriestracking.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "user_favorite_food",
		uniqueConstraints = {@UniqueConstraint(name="UK_favorite_food", columnNames = {"user_id", "food_id"})})
@Data
@RequiredArgsConstructor
@Api("Favorite Food APIs")
public class UserFavoriteFood {
	
	@ApiModelProperty(position = 1, value = "The database generated FavFood ID")
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ApiModelProperty(position = 2, value = "Thông tin User")
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	@ApiModelProperty(position = 3, value = "Thông tin Food mà User đó thêm vào danh sách yêu thích")
	@ManyToOne
	@JoinColumn(name = "food_id")
	private Food food;
}
