package com.caloriestracking.service;

import java.util.List;

import com.caloriestracking.model.Account;

public interface AccountService {
	Account findByUserName(String username);
	
	Boolean checkUsernameExist(String username);

	List<Account> findAll();

	Account save(Account account);

	Account update(Account account);

	void delete(String username);

	Boolean checkUsernameLinkWithUser(String username);
	
}
