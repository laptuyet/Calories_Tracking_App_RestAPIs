package com.caloriestracking.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

import com.caloriestracking.dto.UserDTO;
import com.caloriestracking.mapper.UserDTOMapper;
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
	
	private final UserDTOMapper userDTOMapper;
	
	@GetMapping("/all")
	@PreAuthorize("hasRole('ADMIN')")
	public List<UserDTO> getAllUsers() {
		return userService.findAll()
				.stream()
				.map(userDTOMapper)
				.collect(Collectors.toList());
	}
	
	@GetMapping("{id}")
	public UserDTO getUser(@PathVariable Long id) {
		User user = userService.findById(id);
		return userDTOMapper.apply(user);
	}
	
	@GetMapping("/all/paging")
	@PreAuthorize("hasRole('ADMIN')")
	public List<UserDTO> getAllUsers(
			@RequestParam(defaultValue = "0") Integer pageNo,
			@RequestParam(defaultValue = "5") Integer pageSize,
			@RequestParam(defaultValue = "lastName") String sortBy) {
		return userService.findAll(pageNo, pageSize, sortBy)
				.stream()
				.map(userDTOMapper)
				.collect(Collectors.toList());
	}
	
	@PostMapping("/create")
	public ResponseEntity<UserDTO> createUser(
			@RequestPart("user") String userJson,
			@RequestPart(name = "image", required = false) MultipartFile image)
					throws JsonMappingException, JsonProcessingException {
		return ResponseEntity.ok(
					userDTOMapper.apply(
							userService.save(jsonMapperService.getObjectFromJson(userJson, User.class), image)
					)
				);
	}
	
	@PutMapping("/update")
	public ResponseEntity<UserDTO> updateUser(
			@RequestPart("user") String userJson,
			@RequestPart(name = "image", required = false) MultipartFile image)
					throws JsonMappingException, JsonProcessingException {
		return ResponseEntity.ok(
					userDTOMapper.apply(
							userService.update(jsonMapperService.getObjectFromJson(userJson, User.class), image)
					)
				);
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteUser(@PathVariable Long id) {
		return userService.delete(id);
	}
	
}
