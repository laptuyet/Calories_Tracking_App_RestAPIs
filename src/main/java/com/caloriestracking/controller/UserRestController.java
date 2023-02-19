package com.caloriestracking.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.caloriestracking.model.User;
import com.caloriestracking.service.JsonMapperService;
import com.caloriestracking.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/users")
public class UserRestController {

	private final UserService userService;
	
	private final JsonMapperService jsonMapperService;
	
	@GetMapping("/all")
	public List<User> getAllUsers() {
		return userService.findAll();
	}
	
	@GetMapping("{id}")
	public User getUser(@PathVariable Long id) {
		return userService.findById(id);
	}
	
	@GetMapping("/all/paging")
	public List<User> getAllUsers(
			@RequestParam(defaultValue = "0") Integer pageNo,
			@RequestParam(defaultValue = "5") Integer pageSize,
			@RequestParam(defaultValue = "lastName") String sortBy) {
		return userService.findAll(pageNo, pageSize, sortBy);
	}
	
	@PostMapping("/create")
	public ResponseEntity<User> createUser(
			@RequestPart("user") String userJson,
			@RequestPart(name = "image", required = false) MultipartFile image)
					throws JsonMappingException, JsonProcessingException {
		return ResponseEntity.ok(
				userService.save(jsonMapperService.getObjectFromJson(userJson, User.class), image)
				);
	}
	
	@PutMapping("/update")
	public ResponseEntity<User> updateUser(
			@RequestPart("user") String userJson,
			@RequestPart(name = "image", required = false) MultipartFile image)
					throws JsonMappingException, JsonProcessingException {
		return ResponseEntity.ok(
				userService.update(jsonMapperService.getObjectFromJson(userJson, User.class), image)
				);
	}
	
	@DeleteMapping("/delete/{id}")
	public void deleteUser(@PathVariable Long id) {
		userService.delete(id);
	}
	
}
