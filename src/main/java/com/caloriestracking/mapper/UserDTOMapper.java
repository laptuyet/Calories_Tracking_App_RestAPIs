package com.caloriestracking.mapper;

import java.util.function.Function;

import org.springframework.stereotype.Service;

import com.caloriestracking.dto.UserDTO;
import com.caloriestracking.model.User;

@Service
public class UserDTOMapper implements Function<User, UserDTO>{

	@Override
	public UserDTO apply(User t) {
		return new UserDTO(
				t.getId(),
				t.getFirstName(),
				t.getLastName(),
				t.getImage(),
				t.getDob(),
				t.getGender(),
				t.getWeight(),
				t.getHeight(),
				t.getEmail()
		);
	}
	
}
