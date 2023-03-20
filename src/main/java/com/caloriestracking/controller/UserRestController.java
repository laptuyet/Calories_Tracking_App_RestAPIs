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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/users")
@Api("USer APIs")
public class UserRestController {

	private final UserService userService;
	
	private final JsonMapperService jsonMapperService;
	
	private final UserDTOMapper userDTOMapper;
	
	@ApiOperation(value = "Lấy danh sách tất cả các User")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Lấy thành công",
					response = List.class),
			@ApiResponse(code = 404, message = "Không tìm thấy")
	})
	@GetMapping("/all")
	@PreAuthorize("hasRole('ADMIN')")
	public List<UserDTO> getAllUsers() {
		return userService.findAll()
				.stream()
				.map(userDTOMapper)
				.collect(Collectors.toList());
	}
	
	@ApiOperation(value = "Lấy thông tin của một User bằng userId")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Lấy thành công",
					response = UserDTO.class),
			@ApiResponse(code = 400, message = "Thông tin thêm không hợp lệ"),
			@ApiResponse(code = 404, message = "Không tìm thấy")
	})
	@GetMapping("{id}")
	public UserDTO getUser(
			@ApiParam(value = "ID của User")
			@PathVariable Long id) {
		User user = userService.findById(id);
		return userDTOMapper.apply(user);
	}
	
	@ApiOperation(value = "Lấy danh sách tất cả các User có phân trang")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Lấy thành công",
					response = List.class),
			@ApiResponse(code = 400, message = "Thông tin truyền vào không hợp lệ"),
			@ApiResponse(code = 404, message = "Không tìm thấy")
	})
	@GetMapping("/all/paging")
	@PreAuthorize("hasRole('ADMIN')")
	public List<UserDTO> getAllUsers(
			@ApiParam(value = "Page number", defaultValue = "0")
			@RequestParam(defaultValue = "0") Integer pageNo,
			@ApiParam(value = "Page size", defaultValue = "5")
			@RequestParam(defaultValue = "5") Integer pageSize,
			@ApiParam(value = "Sort type", defaultValue = "Sort by 'lastName'")
			@RequestParam(defaultValue = "lastName") String sortBy) {
		return userService.findAll(pageNo, pageSize, sortBy)
				.stream()
				.map(userDTOMapper)
				.collect(Collectors.toList());
	}
	
	@ApiOperation(value = "Tạo mới User")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Tạo thành công",
					response = UserDTO.class),
			@ApiResponse(code = 400, message = "Thông tin tạo mới không hợp lệ")
	})
	@PostMapping("/create")
	public ResponseEntity<UserDTO> createUser(
			@ApiParam(value = "String User JSON")
			@RequestPart("user") String userJson,
			@ApiParam(value = "Image của User", required = false)
			@RequestPart(name = "image", required = false) MultipartFile image)
					throws JsonMappingException, JsonProcessingException {
		return ResponseEntity.ok(
					userDTOMapper.apply(
							userService.save(jsonMapperService.getObjectFromJson(userJson, User.class), image)
					)
				);
	}
	
	@ApiOperation(value = "Update User")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Update thành công",
					response = UserDTO.class),
			@ApiResponse(code = 400, message = "Thông tin Update không hợp lệ")
	})
	@PutMapping("/update")
	public ResponseEntity<UserDTO> updateUser(
			@ApiParam(value = "String User JSON")
			@RequestPart("user") String userJson,
			@ApiParam(value = "Image của User", required = false)
			@RequestPart(name = "image", required = false) MultipartFile image)
					throws JsonMappingException, JsonProcessingException {
		return ResponseEntity.ok(
					userDTOMapper.apply(
							userService.update(jsonMapperService.getObjectFromJson(userJson, User.class), image)
					)
				);
	}
	
	@ApiOperation(value = "Xóa User")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Xóa thành công",
					response = String.class),
			@ApiResponse(code = 404, message = "Không tìm thấy User ID")
	})
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteUser(
			@ApiParam(value = "ID của User cần xóa")
			@PathVariable Long id) {
		return userService.delete(id);
	}
	
}
