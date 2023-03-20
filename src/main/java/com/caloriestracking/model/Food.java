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
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value = "Food model")
public class Food {
	
	@ApiModelProperty(position = 1, value = "The database generated Food ID")
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ApiModelProperty(position = 2, value = "Tên của Food")
	@Column(unique = true)
	@NotBlank(message = "Food name should not be blank")
	@NotEmpty(message = "Food name should not be empty")
	private String name;
	
	@ApiModelProperty(position = 3, value = "Mô tả về Food")
	private String description;
	
	@ApiModelProperty(position = 4, value = "Ảnh của Food")
	private String image;
	
	@ApiModelProperty(position = 5, value = "Protein/100gr có trong Food")
	@Min(value = 0, message = "Protein must be >= 0")
	private BigDecimal protein;
	
	@ApiModelProperty(position = 6, value = "Fat/100gr có trong Food")
	@Min(value = 0, message = "Fat must be >= 0")
	private BigDecimal fat;
	
	@ApiModelProperty(position = 7, value = "Carb/100gr có trong Food")
	@Min(value = 0, message = "Carb must be >= 0")
	private BigDecimal carb;
	
	@ApiModelProperty(position = 8, value = "Calories/100gr có trong Food")
	@Min(value = 0, message = "Energy per serving index must be >= 0")
	private BigDecimal energyPerServing;
	
	@ApiModelProperty(position = 9, value = "Category chứa Food đó")
	@ManyToOne
	@JoinColumn(name = "category_id")
	private Category category;
	
	@ApiModelProperty(position = 10, value = "Danh sách Tracking mà Food đó xuất hiện")
	@OneToMany(mappedBy = "food", fetch = FetchType.LAZY)
	@JsonIgnore
	private List<UserFoodTracking> userFoodTrackings;
	
	@ApiModelProperty(position = 11, value = "Danh sách Favorite mà Food đó xuất hiện")
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	@JsonIgnore
	private List<UserFavoriteFood> userFavoriteFoods;
}
