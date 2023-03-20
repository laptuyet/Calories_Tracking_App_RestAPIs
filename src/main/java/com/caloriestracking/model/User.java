package com.caloriestracking.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.caloriestracking.utils.AppConstants;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "user")
@Data
@RequiredArgsConstructor
@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "id")
@ApiModel(value = "User model")
public class User implements UserDetails{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(position = 1, value = "The database generated User ID")
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ApiModelProperty(position = 2, value = "Họ của User")
	@NotEmpty(message = "First name should not be empty")
	@NotBlank(message = "First name should not be blank")
	private String firstName;
	
	@ApiModelProperty(position = 3, value = "Tên của User")
	@NotEmpty(message = "Last name should not be empty")
	@NotBlank(message = "Last name should not be blank")
	private String lastName;
	
	@ApiModelProperty(position = 4, value = "Ảnh của User")
	private String image;
	
	@ApiModelProperty(position = 5, value = "Ngày sinh của User")
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate dob;
	
	@ApiModelProperty(position = 6, value = "Giới tính của User: 0-Nam, 1-Nữ")
	private Boolean gender;
	
	@ApiModelProperty(position = 7, value = "Cân nặng của User")
	@Min(value = 0, message = "Weight should be >= 0")
	private BigDecimal weight;
	
	@ApiModelProperty(position = 8, value = "Chiều cao của User")
	@Min(value = 0, message = "Height should be >= 0")
	private BigDecimal height;
	
	@ApiModelProperty(position = 9, value = "Email của User")
	@Column(unique = true)
	@NotEmpty(message = "Email should not be empty")
	@NotBlank(message = "Email should not be blank")
	@Email(message = "Invalid email pattern", 
		regexp = AppConstants.EMAIL_REGEX_PATTERN)
	private String email;
	
	@ApiModelProperty(position = 10, value = "Account mà User đó liên kết")
	@OneToOne
	@JoinColumn(name = "username", referencedColumnName = "username")
	private Account account;
	
	@ApiModelProperty(position = 11, value = "Danh sách Tracking mà User đó xuất hiện")
	@OneToMany(mappedBy = "user")
	@JsonIgnore
	private List<UserFoodTracking> userFoodTrackings;
	
	@ApiModelProperty(position = 12, value = "Danh sách Favorite mà User đó xuất hiện")
	@OneToMany(mappedBy = "user")
	@JsonIgnore
	private List<UserFavoriteFood> userFavoriteFoods;
	
	@ApiModelProperty(position = 13, value = "Danh sách các token của User đó")
	@OneToMany(mappedBy = "user")
	@JsonIgnore
	private List<Token> tokens;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority(this.getAccount().getRole().name()));
	}

	@Override
	public String getPassword() {
		return this.getAccount().getPassword();
	}

	@Override
	public String getUsername() {
		return this.getAccount().getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
