package com.caloriestracking.repo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.caloriestracking.model.User;

public interface UserRepository extends JpaRepository<User, Long>{
	Optional<User> findByEmail(String email);
	
	@Query("""
			select u1 from User u1 where :newEmail in (select u.email from User u where u.id != :userId)
			""")
	List<User> checkNewEmail(Long userId, String newEmail);
}
