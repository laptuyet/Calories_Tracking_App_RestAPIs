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
@ApiModel(value = "Category model", description = "Danh mục đồ ăn")
public class Category {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@ApiModelProperty(position = 1, notes = "The database generated Category ID")
	private Long id;
	
	@NotNull(message = "Category name should not be null")
	@NotEmpty(message = "Category name should not be empty")
	@Column(unique = true)
	private String name;
	
	private String image;
	
	@OneToMany(mappedBy = "category")
	private List<Food> foods;
}
