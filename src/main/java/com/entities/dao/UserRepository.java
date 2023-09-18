package com.entities.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.entities.user.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	User findByEmail(String Email);
}
