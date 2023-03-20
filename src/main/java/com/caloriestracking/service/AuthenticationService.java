package com.caloriestracking.service;

import javax.transaction.Transactional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.caloriestracking.dto.AuthenticationReponse;
import com.caloriestracking.dto.AuthenticationRequest;
import com.caloriestracking.dto.EmailMessage;
import com.caloriestracking.dto.ForgetRequest;
import com.caloriestracking.dto.RegisterRequest;
import com.caloriestracking.exceptions.ResourceNotFoundException;
import com.caloriestracking.jwt.JwtService;
import com.caloriestracking.model.Account;
import com.caloriestracking.model.Role;
import com.caloriestracking.model.Token;
import com.caloriestracking.model.TokenType;
import com.caloriestracking.model.User;
import com.caloriestracking.repo.TokenRepository;
import com.caloriestracking.repo.UserRepository;
import com.caloriestracking.utils.AppConstants;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
	
	private final AuthenticationManager authenticationManager;
	
	private final JwtService jwtService;
	
	private final AccountService accountService;
	
	private final UserRepository userRepository;
	
	private final TokenRepository tokenRepository;
	
	private final MailService mailService;

	public AuthenticationReponse authenticate(AuthenticationRequest request) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
		);
		
		if(authentication.isAuthenticated()) {
			User user = accountService.findByUserName(request.getUsername()).getUser();
			String jwtToken = jwtService.generateToken(user);
			
			revokeAllUserToken(user);
			saveUserToken(user, jwtToken);
			
			return  AuthenticationReponse
					.builder()
					.token(jwtToken)
					.build();
		}
		else throw new ResourceNotFoundException("Invalid request!");
	}

	public AuthenticationReponse register(RegisterRequest request) {
		Account account = new Account();
		User user = new User();
		user.setFirstName(request.getFirstName());
		user.setLastName(request.getLastName());
		user.setEmail(request.getEmail());
		
		account.setUsername(request.getUsername());
		account.setPassword(request.getPassword());
		account.setUser(user);
		
		if (request.getRole() == null) account.setRole(Role.ROLE_USER);
		else account.setRole(request.getRole());
		 
		User user_linked_with_account = accountService.save(account).getUser();
		String jwtToken = jwtService.generateToken(user_linked_with_account);
		
		revokeAllUserToken(user_linked_with_account);
		saveUserToken(user_linked_with_account, jwtToken);
		
		return AuthenticationReponse
				.builder()
				.token(jwtToken)
				.build();
	}
	
	private void revokeAllUserToken(User user) {
		var validTokens = tokenRepository.findAllValidTokenByUser(user.getId());
		
		if(validTokens.isEmpty()) return;
		
		validTokens.forEach(t -> {
			t.setExpired(true);
			t.setRevoked(true);
		});
		
		tokenRepository.saveAll(validTokens);
	}
	
	private void saveUserToken(User user, String jwtToken) {
		Token token = Token.builder()
				.user(user)
				.token(jwtToken)
				.tokenType(TokenType.BEARER)
				.expired(false)
				.revoked(false)
				.build();
		
		tokenRepository.save(token);
	}

	@Transactional
	public String forgot(ForgetRequest request) {
		
		String usernameOrEmail = request.getUsernameOrEmail();
		String emailRegex = AppConstants.EMAIL_REGEX_PATTERN;
		String emailOfFoundUser = "";
		Account forgotAccount = null;
		
		if (usernameOrEmail != null && !usernameOrEmail.isBlank()) {
			if (usernameOrEmail.trim().matches(emailRegex)) {
				User user = userRepository.findByEmail(usernameOrEmail)
						.orElseThrow(() -> new ResourceNotFoundException(
								String.format("User with email <%s> not found", usernameOrEmail)));
				forgotAccount = user.getAccount();
				emailOfFoundUser = user.getEmail();
			} else {
				forgotAccount = accountService.findByUserName(usernameOrEmail);
				emailOfFoundUser = forgotAccount.getUser().getEmail();
			}
		} else throw new ResourceNotFoundException("'usernameOrEmail' is missing in the request object");
		
		forgotAccount.setPassword(new BCryptPasswordEncoder().encode("12345"));
		
		EmailMessage email = EmailMessage
				.builder()
				.from("hiep000000006@gmail.com")
				.to(emailOfFoundUser)
				.subject("Reset password for Forgoten Account")
				.text("Password is: 12345, please change after login!")
				.build();
		
		mailService.send(email);
		
		return String.format("Check email <%s> to get new password", emailOfFoundUser);
	}

}
