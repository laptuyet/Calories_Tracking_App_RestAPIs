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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.caloriestracking.exceptions.ExistingResourceException;
import com.caloriestracking.exceptions.ResourceNotFoundException;
import com.caloriestracking.model.Account;
import com.caloriestracking.model.User;
import com.caloriestracking.repo.UserRepository;
import com.caloriestracking.service.AccountService;
import com.caloriestracking.service.CloudinaryService;
import com.caloriestracking.service.UserService;
import com.caloriestracking.validator.ObjectsValidator;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
	
	private static Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
	
	private final UserRepository userRepository;
	
	private final ObjectsValidator<User> validator;
	
	private final CloudinaryService cloudinaryService;
	
	private final AccountService accountService;

	@Override
	public User findById(Long id) {
		return userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));
	}
	
	@Override
	public User findByUsername(String username) {
		return accountService.findByUserName(username).getUser();
	}

	@Override
	public List<User> findAll() {
		return userRepository.findAll();
	}
	
	@Override
	public List<User> findAll(Integer pageNo, Integer pageSize, String sortBy) {
		Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
		Slice<User> slicePage = userRepository.findAll(paging);
		return slicePage.getContent();
	}

	@Override
	public User save(User user, MultipartFile file) {
		
		log.info("Validate object: " + user);
		validator.validate(user);
		
//		if (user.getId() != null) {
//			if (userRepository.findById(user.getId()).isPresent()) {
//				throw new ExistingResourceException("User with id <" + user.getId() + "> is existing");
//			}
//		}
		
		// Kiểm tra xem account có trong json hay không
		Account account = user.getAccount();
		if (account != null) {
			if(account.getUsername() != null || !account.getUsername().isBlank()) {
				if(accountService.checkUsernameLinkWithUser(account.getUsername()))
					throw new ExistingResourceException("Account with username <" + account.getUsername() 
														+ "> is linked with another User");
			} else
				throw new ResourceNotFoundException(
					" 'username' field of this account is empty or blank, must specify username for this account");
		} else
			throw new ResourceNotFoundException(" 'account' field is not included in this user JSON, "
					+ "\n please create new account and then assign to this user");
		
		// Nếu có ảnh thì setImage cho user đó
		if (file != null && !file.isEmpty()) {
			String imageURL = cloudinaryService.uploadImage(file);
			user.setImage(imageURL);
		}
		
		// Link this user to the account in request object
		user.setAccount(account);
		
		return userRepository.save(user);
	}

	@Override
	public User update(User user, MultipartFile file) {
		log.info("Validate object: " + user);
		validator.validate(user);
		
		User foundUser = userRepository.findById(user.getId()).orElseThrow(
				() -> new ResourceNotFoundException("User with id <" + user.getId() + "> not found"));
		
		if (userRepository.checkNewEmail(foundUser.getId(), user.getEmail()).size() > 0)
			throw new ExistingResourceException("User with email <" + user.getEmail() + "> is existing!");

		if (file != null && !file.isEmpty()) {
			String imageURL = cloudinaryService.uploadImage(file);
			foundUser.setImage(imageURL);
		}
		
		foundUser.setFirstName(user.getFirstName());
		foundUser.setLastName(user.getLastName());
		foundUser.setDob(user.getDob());
		foundUser.setGender(user.getGender());
		foundUser.setWeight(user.getWeight());
		foundUser.setHeight(user.getHeight());
		foundUser.setEmail(user.getEmail());
		
		return userRepository.save(foundUser);
	}

	@Override
	public ResponseEntity<String> delete(Long userId) {
		
		User user = findById(userId);
		
		Account account = accountService.findByUserName(user.getAccount().getUsername());
		
		return accountService.delete(account.getUsername());
	}

}
