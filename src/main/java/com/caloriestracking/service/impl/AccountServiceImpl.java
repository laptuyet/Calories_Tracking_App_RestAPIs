package com.caloriestracking.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.caloriestracking.exceptions.ResourceNotFoundException;
import com.caloriestracking.model.Account;
import com.caloriestracking.repo.AccountRepository;
import com.caloriestracking.service.AccountService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService{
	
	private final AccountRepository accountRepository;

	@Override
	public Account findByUserName(String username) {
		return accountRepository.findByUsername(username)
				.orElseThrow(() -> new ResourceNotFoundException("Account with id <" + username + "> not found"));
	}

	@Override
	public List<Account> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Account save(Account account) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Account update(Account account) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(String username) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Boolean checkUsernameExist(String username) {
		return accountRepository.findByUsername(username).get() != null;
	}

	@Override
	public Boolean checkUsernameLinkWithUser(String username) {
		Account account = findByUserName(username);
		return account != null && account.getUser() != null;
	}

}
