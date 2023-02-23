package com.caloriestracking.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
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
import com.caloriestracking.mapper.AccountDTOMapper;
import com.caloriestracking.model.Account;
import com.caloriestracking.service.AccountService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/accounts")
public class AccountRestController {

	private final AccountService accountService;
	
	private final AccountDTOMapper accountDTOMapper;
	
	@GetMapping("{username}")
	public AccountDTO getAccount(@PathVariable String username) {
		AccountDTO accDto = accountDTOMapper.apply(accountService.findByUserName(username));
		return accDto;
	}
	
	@GetMapping("/all")
	public List<AccountDTO> getAllAccounts() {
		List<Account> accounts = accountService.findAll();
		return accounts.stream()
				.map(accountDTOMapper)
				.collect(Collectors.toList());
	}
	
	@GetMapping("/all/paging")
	public List<AccountDTO> getAllAccounts(
			@RequestParam(defaultValue = "0") Integer pageNo,
			@RequestParam(defaultValue = "5") Integer pageSize,
			@RequestParam(defaultValue = "username") String sortBy) {
		return accountService.findAll(pageNo, pageSize, sortBy)
				.stream()
				.map(accountDTOMapper)
				.collect(Collectors.toList());
	}
	
	@PostMapping("/create")
	public ResponseEntity<AccountDTO> createAccount(@RequestBody Account account) {
		return ResponseEntity.ok(
				accountDTOMapper.apply(accountService.save(account))
				);
	}
	
	@PutMapping("/update")
	public ResponseEntity<AccountDTO> updateAccount(@RequestBody Account account) {
		return ResponseEntity.ok(
				accountDTOMapper.apply(accountService.update(account))
				);
	}
	
	@DeleteMapping("/delete/{username}")
	public ResponseEntity<String> deleteUser(@PathVariable String username) {
		return accountService.delete(username);
	}
}
