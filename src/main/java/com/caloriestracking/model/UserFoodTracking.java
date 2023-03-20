package com.caloriestracking.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "user_food_tracking")
@Data
@RequiredArgsConstructor
@Api("Favorite Food APIs")
public class UserFoodTracking {
	
	@ApiModelProperty(position = 1, value = "The database generated TrackingFood ID")
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ApiModelProperty(position = 2, value = "Thông tin User")
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	@ApiModelProperty(position = 3, value = "Thông tin Food mà User đó thêm vào danh sách tracking (ds food đã tiêu thụ)")
	@ManyToOne
	@JoinColumn(name = "food_id")
	private Food food;
	
	@ApiModelProperty(position = 4, value = "Số gram tiêu thụ cho mỗi lần thêm vào danh sách tracking")
	private BigDecimal consumedGram;
	
	@ApiModelProperty(position = 5, value = "Thời điểm tiêu thụ")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime consumedDatetime;
}
