package com.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.entities.user.UserDto;

public interface UserService extends UserDetailsService {

	public UserDto login(String email);

	public void save(UserDto userDto);
	public List<UserDto> findAllUsers();
}
