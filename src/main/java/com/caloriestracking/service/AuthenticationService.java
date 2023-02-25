package com.caloriestracking.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.caloriestracking.dto.AuthenticationReponse;
import com.caloriestracking.dto.AuthenticationRequest;
import com.caloriestracking.dto.RegisterRequest;
import com.caloriestracking.exceptions.ResourceNotFoundException;
import com.caloriestracking.jwt.JwtService;
import com.caloriestracking.model.Account;
import com.caloriestracking.model.Role;
import com.caloriestracking.model.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
	
	private final AuthenticationManager authenticationManager;
	
	private final JwtService jwtService;
	
	private final AccountService accountService;

	public AuthenticationReponse authenticate(AuthenticationRequest request) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
		);
		
		if(authentication.isAuthenticated()) {
			User user = accountService.findByUserName(request.getUsername()).getUser();
			return  AuthenticationReponse
					.builder()
					.token(jwtService.generateToken(user))
					.build();
		}
		else throw new ResourceNotFoundException("Invalid request!");
	}

	public AuthenticationReponse register(RegisterRequest request) {
		Account account = new Account();
		User user = new User();
		user.setFirstName(request.getFirstName());
		user.setLastName(request.getLastName());
		
		account.setUsername(request.getUsername());
		account.setPassword(request.getPassword());
		account.setUser(user);
		
		if (request.getRole() == null) account.setRole(Role.ROLE_USER);
		else account.setRole(request.getRole());
		
		User user_linked_with_account = accountService.save(account).getUser();
		
		return AuthenticationReponse
				.builder()
				.token(jwtService.generateToken(user_linked_with_account))
				.build();
	}

}
