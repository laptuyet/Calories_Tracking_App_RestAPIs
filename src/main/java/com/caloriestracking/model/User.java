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

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "user")
@Data
@RequiredArgsConstructor
@JsonIdentityInfo(
		  generator = ObjectIdGenerators.PropertyGenerator.class, 
		  property = "id")
public class User implements UserDetails{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotEmpty(message = "First name should not be empty")
	@NotBlank(message = "First name should not be blank")
	private String firstName;
	
	@NotEmpty(message = "Last name should not be empty")
	@NotBlank(message = "Last name should not be blank")
	private String lastName;
	
	private String image;
	
	@JsonFormat(pattern = "yyyy-MM-dd")
	private LocalDate dob;
	
	private Boolean gender;
	
	@Min(value = 0, message = "Weight should be >= 0")
	private BigDecimal weight;
	
	@Min(value = 0, message = "Height should be >= 0")
	private BigDecimal height;
	
	@Column(unique = true)
	@Email(message = "Invalid email pattern")
	private String email;
	
	@OneToOne
	@JoinColumn(name = "username", referencedColumnName = "username")
	private Account account;
	
	@OneToMany(mappedBy = "user")
	@JsonIgnore
	private List<UserFoodTracking> userFoodTrackings;
	
	@OneToMany(mappedBy = "user")
	@JsonIgnore
	private List<UserFavoriteFood> userFavoriteFoods;

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
