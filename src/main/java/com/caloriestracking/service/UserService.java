package com.caloriestracking.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.caloriestracking.model.User;

public interface UserService {

	User findById(Long id);
	
	User findByUsername(String username);

	List<User> findAll();
	
	List<User> findAll(Integer pageNo, Integer pageSize, String sortBy);

	User save(User user, MultipartFile file);

	User update(User user, MultipartFile file);

	ResponseEntity<String> delete(Long userId);
}
