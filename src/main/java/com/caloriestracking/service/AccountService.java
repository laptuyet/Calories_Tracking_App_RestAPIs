package com.caloriestracking.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.caloriestracking.dto.ChangePwdRequest;
import com.caloriestracking.model.Account;

public interface AccountService {
	Account findByUserName(String username);
	
	Boolean checkUsernameExist(String username);

	List<Account> findAll();
	
	List<Account> findAll(Integer pageNo, Integer pageSize, String sortBy);

	Account save(Account account);

	Account update(Account account);

	ResponseEntity<String> delete(String username);

	Boolean checkUsernameLinkWithUser(String username);

	ResponseEntity<String> changePwd(ChangePwdRequest request);
	
}
