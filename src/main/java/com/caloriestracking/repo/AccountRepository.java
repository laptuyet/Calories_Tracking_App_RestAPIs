package com.caloriestracking.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.caloriestracking.model.Account;

public interface AccountRepository extends JpaRepository<Account, Long>{

	Optional<Account> findByUsername(String username);
	
	void deleteByUsername(String username);
}
