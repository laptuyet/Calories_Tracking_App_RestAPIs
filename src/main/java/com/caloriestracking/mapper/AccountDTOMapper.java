package com.caloriestracking.mapper;

import java.util.function.Function;

import org.springframework.stereotype.Service;

import com.caloriestracking.dto.AccountDTO;
import com.caloriestracking.model.Account;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountDTOMapper implements Function<Account, AccountDTO> {
	
	private final UserDTOMapper userDTOMapper;

	@Override
	public AccountDTO apply(Account t) {
		
		return new AccountDTO(
				t.getUsername(),
				t.getRole(),
				userDTOMapper.apply(t.getUser())
		);
	}

}
