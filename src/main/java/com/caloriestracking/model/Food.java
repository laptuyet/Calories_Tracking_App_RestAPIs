package com.caloriestracking.model;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "food")
@Data
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "id")
public class Food {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(unique = true)
	@NotBlank(message = "Food name should not be blank")
	@NotEmpty(message = "Food name should not be empty")
	private String name;
	
	private String description;
	
	private String image;
	
	@Min(value = 0, message = "Protein must be >= 0")
	private BigDecimal protein;
	
	@Min(value = 0, message = "Fat must be >= 0")
	private BigDecimal fat;
	
	@Min(value = 0, message = "Carb must be >= 0")
	private BigDecimal carb;
	
	@Min(value = 0, message = "Energy per serving index must be >= 0")
	private BigDecimal energyPerServing;
	
	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;
	
	@OneToMany(mappedBy = "food", fetch = FetchType.LAZY)
	private List<UserFoodTracking> userFoodTrackings;
	
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	private List<UserFavoriteFood> userFavoriteFoods;
}
