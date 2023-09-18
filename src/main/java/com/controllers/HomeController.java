package com.controllers;

import java.util.List;

import org.apache.coyote.http11.Http11InputBuffer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.confg.LoginPayload;
import com.entities.dao.UserRepository;
import com.entities.user.User;
import com.entities.user.UserDto;
import com.service.UserServiceImpl;

@RestController
@RequestMapping("/user")
public class HomeController {

	@Autowired
	private UserServiceImpl userService;
	

	
	@GetMapping("/all")
	public List<UserDto> findAllUsers(){
		return userService.findAllUsers();
	}
	
	@PostMapping("/save")
    public String saveuser(@RequestBody UserDto userDto) {
		userService.save(userDto);
	
          return "success adtion";
    }
	
	
	


}
