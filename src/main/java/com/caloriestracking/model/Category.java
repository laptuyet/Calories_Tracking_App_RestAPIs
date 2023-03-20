package com.caloriestracking.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "category")
@Data
@RequiredArgsConstructor
@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "id")
@ApiModel(value = "Category model")
public class Category {
	
	@ApiModelProperty(position = 1, value = "The database generated Category ID")
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ApiModelProperty(position = 2, value = "Tên của Category")
	@NotNull(message = "Category name should not be null")
	@NotEmpty(message = "Category name should not be empty")
	@Column(unique = true)
	private String name;
	
	@ApiModelProperty(position = 3, value = "Ảnh của Category")
	private String image;
	
	@ApiModelProperty(position = 4, value = "Danh sách các Food của Category đó")
	@OneToMany(mappedBy = "category")
	private List<Food> foods;
}
