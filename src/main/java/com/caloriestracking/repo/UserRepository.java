package com.caloriestracking.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.caloriestracking.model.User;

public interface UserRepository extends JpaRepository<User, Long>{

}
