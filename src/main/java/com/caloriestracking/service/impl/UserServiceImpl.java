package com.caloriestracking.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
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
		
		Account account = user.getAccount();
		if (account != null) {
			if(account.getUsername() != null || !account.getUsername().isBlank()) {
				if(accountService.checkUsernameLinkWithUser(account.getUsername()))
					throw new ExistingResourceException("Account with username <" + account.getUsername() 
														+ "> is linked with a User");
			} else
				throw new ResourceNotFoundException(
					" 'username' field of this account is empty or blank, must specify username for this account");
		} else
			throw new ResourceNotFoundException(" 'account' field is not included in this user JSON, "
					+ "\n please create new account and then assign to this user");
		
		if (file != null && !file.isEmpty()) {
			String imageURL = cloudinaryService.uploadImage(file);
			user.setImage(imageURL);
		}
		
		user.setAccount(account);
		
		return userRepository.save(user);
		
		// đổi mapping user -> account ở cột username trong user class
	}

	@Override
	public User update(User user, MultipartFile file) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(Long userId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(String username) {
		// TODO Auto-generated method stub
		
	}

}
