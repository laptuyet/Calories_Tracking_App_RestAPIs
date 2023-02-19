package com.caloriestracking.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "user_favorite_food",
		uniqueConstraints = {@UniqueConstraint(name="UK_favorite_food", columnNames = {"user_id", "food_id"})})
@Data
@RequiredArgsConstructor
public class UserFavoriteFood {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	@ManyToOne
	@JoinColumn(name = "food_id")
	private Food food;
}
