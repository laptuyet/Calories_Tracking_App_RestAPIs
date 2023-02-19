package com.caloriestracking.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "user_food_tracking")
@Data
@RequiredArgsConstructor
public class UserFoodTracking {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	@ManyToOne
	@JoinColumn(name = "food_id")
	private Food food;
	
	private Double consumedGram;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date consumedDatetime;
}
