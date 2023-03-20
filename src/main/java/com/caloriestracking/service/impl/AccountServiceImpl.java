package com.caloriestracking.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.caloriestracking.dto.ChangePwdRequest;
import com.caloriestracking.dto.UserDTO;
import com.caloriestracking.exceptions.ExistingResourceException;
import com.caloriestracking.exceptions.ResourceNotFoundException;
import com.caloriestracking.mapper.UserDTOMapper;
import com.caloriestracking.model.Account;
import com.caloriestracking.model.Role;
import com.caloriestracking.model.User;
import com.caloriestracking.model.UserFavoriteFood;
import com.caloriestracking.model.UserFoodTracking;
import com.caloriestracking.repo.AccountRepository;
import com.caloriestracking.repo.UserFavoriteFoodRepository;
import com.caloriestracking.repo.UserFoodTrackingRepository;
import com.caloriestracking.repo.UserRepository;
import com.caloriestracking.service.AccountService;
import com.caloriestracking.validator.ObjectsValidator;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService{
	
	private static Logger log = LoggerFactory.getLogger(AccountServiceImpl.class);
	
	private final AccountRepository accountRepository;
	
	private final UserRepository userRepository;
	
	private final ObjectsValidator<Account> validator;
	
	private final ObjectsValidator<User> userValidator;
	
	private final UserDTOMapper userDTOMapper;
	
	private final UserFavoriteFoodRepository favoriteFoodRepository;
	
	private final UserFoodTrackingRepository trackingRepository;
	
	private final PasswordEncoder passwordEncoder;

	@Override
	public Account findByUserName(String username) {
		return accountRepository.findByUsername(username)
				.orElseThrow(() -> new ResourceNotFoundException("Account with username <" + username + "> not found"));
	}

	@Override
	public List<Account> findAll() {
		return accountRepository.findAll();
	}
	
	@Override
	public List<Account> findAll(Integer pageNo, Integer pageSize, String sortBy) {
		Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
		Slice<Account> slicePage = accountRepository.findAll(paging);
		return slicePage.getContent();
	}

	@Override
	public Account save(Account account) {
		log.info("Validate object: " + account);
		validator.validate(account);
		
		if( accountRepository.findByUsername(account.getUsername().trim()).isPresent()) {
			throw new ExistingResourceException("Account with username <" 
													+ account.getUsername().trim() + "> is existed.");
		}
		
		User user = account.getUser();
		if(user != null) {
			log.info("Validate object: " + user);
			userValidator.validate(user);
			
			if (userRepository.findByEmail(user.getEmail()).isPresent())
				throw new ExistingResourceException("User with email <" + user.getEmail() + "> is existing!");
			
		} else 
			throw new ResourceNotFoundException(" 'user' field is not present in this account JSON, "
					+ "\n please include 'user' field in this account JSON.");
		
		// Mặc định là Role USER
		if(account.getRole() == null || account.getRole().name().isBlank()) {
			account.setRole(Role.ROLE_USER);
		}
		
		// Encrypt raw password
		String rawPwd = account.getPassword();
		account.setPassword(passwordEncoder.encode(rawPwd));
		
		// Link this user to account in request object
		user.setAccount(account);
		
		return accountRepository.save(account);
	}

	@Override
	public Account update(Account account) {
		log.info("Validate object: " + account);
		validator.validate(account);
		
		Account foundAccount = findByUserName(account.getUsername());
		
		// Encrypt raw password
		foundAccount.setPassword(passwordEncoder.encode(account.getPassword()));
		
		if(account.getRole() != null)
			foundAccount.setRole(account.getRole());
		
		return accountRepository.save(foundAccount);
	}

	@Override
	public ResponseEntity<String> delete(String username) {
		
		Account foundAccount = findByUserName(username);
		
		User user_Linked_With_This_Account = foundAccount.getUser();
		
		Long userId = user_Linked_With_This_Account.getId();
		
		UserDTO userDto = userDTOMapper.apply(user_Linked_With_This_Account);
		
		log.info("Deleting account linked with user: " + userDto);
		
		List<UserFavoriteFood> favFoods = favoriteFoodRepository.findFavorListByUser_Id(userId);
		
		List<UserFoodTracking> trackingFoods = trackingRepository.findFoodTrackingByUser_Id(userId);
		
		if (!favFoods.isEmpty()) {
			log.info("Favorite list size: " + favFoods.size());
			throw new RuntimeException("User with id <" + userId 
					+ "> linked with account(" + username + ") has favorite list, can't be deleted");
		}
		if (!trackingFoods.isEmpty()) {
			log.info("Tracking list size: " + trackingFoods.size());
			throw new RuntimeException("User with id <" + userId 
					+ "> linked with account(" + username + ") has tracking list, can't be deleted");
		}
		
		userRepository.deleteById(userId);
		accountRepository.deleteByUsername(username);
		
		return ResponseEntity.ok("Delete User with <" + userId + ">\n"
				+ "and Account(" + username + ") linked with this user successfully!");
	}
	
	@Override
	public ResponseEntity<String> changePwd(ChangePwdRequest request) {
		Account acc = findByUserName(request.getUsername());
		if (!passwordEncoder.matches(request.getPassword(), acc.getPassword())) {
			throw new ResourceNotFoundException("Old password is incorrect!");
		}
		acc.setPassword(passwordEncoder.encode(request.getNewPassword()));
		accountRepository.save(acc);
		return ResponseEntity.ok("Change password successfully!");
	}

	@Override
	public Boolean checkUsernameExist(String username) {
		return accountRepository.findByUsername(username).get() != null;
	}

	@Override
	public Boolean checkUsernameLinkWithUser(String username) {
		Account account = findByUserName(username);
		return account != null && account.getUser() != null;
	}

}
