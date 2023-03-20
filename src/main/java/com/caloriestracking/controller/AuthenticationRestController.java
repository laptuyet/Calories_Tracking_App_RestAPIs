package com.caloriestracking.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.caloriestracking.dto.AuthenticationReponse;
import com.caloriestracking.dto.AuthenticationRequest;
import com.caloriestracking.dto.ForgetRequest;
import com.caloriestracking.dto.RegisterRequest;
import com.caloriestracking.service.AuthenticationService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/auth")
@Api(value = "Authentication APIs")
public class AuthenticationRestController {

	private final AuthenticationService authService;
	
	@ApiOperation(value = "Đăng nhập")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Lấy thông tin Account thành công",
					response = AuthenticationReponse.class),
			@ApiResponse(code = 404, message = "Không tìm Account")
	})
	@PostMapping("/login")
	public ResponseEntity<AuthenticationReponse> authenticateLogin(@RequestBody AuthenticationRequest request) {
		return ResponseEntity.ok(authService.authenticate(request));
	}
	
	@ApiOperation(value = "Đăng ký")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Đăng ký Account thành công",
					response = AuthenticationReponse.class),
			@ApiResponse(code = 400, message = "Thông tin trong json không hợp lệ")
	})
	@PostMapping("/register")
	public ResponseEntity<AuthenticationReponse> register(@RequestBody RegisterRequest request) {
		return ResponseEntity.ok(authService.register(request));
	}
	
	@PostMapping("/forgot")
	public ResponseEntity<?> forgot(@RequestBody ForgetRequest request) {
		return ResponseEntity.ok(authService.forgot(request));
	}
}
