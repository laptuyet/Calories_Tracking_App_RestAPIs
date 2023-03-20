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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.caloriestracking.dto.AccountDTO;
import com.caloriestracking.dto.ChangePwdRequest;
import com.caloriestracking.mapper.AccountDTOMapper;
import com.caloriestracking.model.Account;
import com.caloriestracking.service.AccountService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/accounts")
@Api(value = "Account APIs")
public class AccountRestController {

	private final AccountService accountService;
	
	private final AccountDTOMapper accountDTOMapper;
	
	@ApiOperation(value = "Lấy thông tin về account bằng username")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Lấy thông tin Account thành công",
					response = Account.class),
			@ApiResponse(code = 404, message = "Không tìm Account")
	})
	@GetMapping("{username}")
	public AccountDTO getAccount(@ApiParam(value = "Username của account") @PathVariable String username) {
		AccountDTO accDto = accountDTOMapper.apply(accountService.findByUserName(username));
		return accDto;
	}
	
	@ApiOperation(value = "Xem danh sách các Account", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Lấy danh sách Account thành công",
					response = Account.class,
					responseContainer = "List"),
			@ApiResponse(code = 404, message = "Không tìm thấy danh sách Account")
	})
	@GetMapping("/all")
	@PreAuthorize("hasRole('ADMIN')")
	public List<AccountDTO> getAllAccounts() {
		List<Account> accounts = accountService.findAll();
		return accounts.stream()
				.map(accountDTOMapper)
				.collect(Collectors.toList());
	}
	
	@ApiOperation(value = "Xem danh sách có phân trang các Account ", response = List.class)
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Lấy danh sách Account thành công",
					response = Account.class,
					responseContainer = "List"),
			@ApiResponse(code = 404, message = "Không tìm thấy danh sách Account")
	})
	@GetMapping("/all/paging")
	@PreAuthorize("hasRole('ADMIN')")
	public List<AccountDTO> getAllAccounts(
			@ApiParam(value = "Page number", defaultValue = "0")
			@RequestParam(defaultValue = "0") Integer pageNo,
			@ApiParam(value = "Page size", defaultValue = "5")
			@RequestParam(defaultValue = "5") Integer pageSize,
			@ApiParam(value = "Sort type", defaultValue = "Sort by 'username'")
			@RequestParam(defaultValue = "username") String sortBy) {
		return accountService.findAll(pageNo, pageSize, sortBy)
				.stream()
				.map(accountDTOMapper)
				.collect(Collectors.toList());
	}
	
	@ApiOperation(value = "Tạo mới Account")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Tạo Account thành công",
					response = Account.class),
			@ApiResponse(code = 400, message = "Thông tin bị thiếu hoặc không hợp lệ")
	})
	@PostMapping("/create")
	public ResponseEntity<AccountDTO> createAccount(
			@ApiParam(value = "Đối tượng Account truyền vào Formdata")
			@RequestBody Account account) {
		return ResponseEntity.ok(
				accountDTOMapper.apply(accountService.save(account))
				);
	}
	
	@ApiOperation(value = "Cập nhật Account")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Cập nhật Account thành công"
					, response = Account.class),
			@ApiResponse(code = 400, message = "Thông tin bị thiếu hoặc không hợp lệ")
	})
	@PutMapping("/update")
	public ResponseEntity<AccountDTO> updateAccount(
			@ApiParam(value = "Đối tượng Account truyền vào Formdata")
			@RequestBody Account account) {
		return ResponseEntity.ok(
				accountDTOMapper.apply(accountService.update(account))
				);
	}
	
	@ApiOperation(value = "Xóa Account")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Xóa Account thành công"
					, response = String.class),
			@ApiResponse(code = 400, message = "Thông tin bị thiếu hoặc không hợp lệ"),
			@ApiResponse(code = 404, message = "Không tìm thấy username")
	})
	@DeleteMapping("/delete/{username}")
	public ResponseEntity<String> deleteUser(
			@ApiParam(value = "User name của Account muốn xóa", example = "")
			@PathVariable String username) {
		return accountService.delete(username);
	}
	
	@ApiOperation(value = "Đổi Password Account")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Đổi Password thành công"
					, response = String.class),
			@ApiResponse(code = 400, message = "Thông tin bị thiếu hoặc không hợp lệ"),
			@ApiResponse(code = 404, message = "Không tìm thấy username hoặc password không đúng")
	})
	@PutMapping("/changePwd")
	public ResponseEntity<String> changePassword(
			@ApiParam(value = "ChangwdReqeust object trong body của request")
			@RequestBody ChangePwdRequest request
			){
		return accountService.changePwd(request);
	}
}
