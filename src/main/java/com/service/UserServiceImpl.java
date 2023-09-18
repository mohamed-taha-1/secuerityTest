package com.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.entities.dao.UserRepository;
import com.entities.user.User;
import com.entities.user.UserDto;
import com.entities.user.UserMapper;

import jakarta.annotation.PostConstruct;

@Service
public class UserServiceImpl implements UserService {

	private final UserRepository userRepo;

	private final UserMapper userMapper;
	private final BCryptPasswordEncoder encode;

	@Autowired
	public UserServiceImpl(UserRepository userRepo, UserMapper userMapper, BCryptPasswordEncoder encode) {
		super();
		this.userRepo = userRepo;
		this.userMapper = userMapper;
		this.encode = encode;
	}

	@PostConstruct
	public void saveData() {

		if (userRepo.count() <= 4) {
			User user1 = new User();
			user1.setEmail("ahmed@gmail.com");
			user1.setPassword(encode.encode("1234"));

			User user2 = new User();
			user2.setEmail("yaser@gmail.com");
			user2.setPassword(encode.encode("1234"));

			User user3 = new User();
			user3.setEmail("ismail@gmail.com");
			user3.setPassword(encode.encode("1234"));

			User user4 = new User();
			user4.setEmail("shahin@gmail.com");
			user4.setPassword(encode.encode("1234"));

			User[] allUsers = { user1, user2, user3, user4 };
			for (User userIndex : allUsers) {
				userRepo.save(userIndex);
			}
		}
	}

	@Override
	public UserDto login(String email) {
		User user = userRepo.findByEmail(email);
		UserDto dto = userMapper.entityToDto(user);
		return dto;
	}

	@Override
	public void save(UserDto userDto) {
		String email = userDto.getEmail();
		User userTest = userRepo.findByEmail(email);
		if (userTest != null)
			throw new UsernameNotFoundException("Try with anther email");
		UserDto local = new UserDto();
		local.setEmail(userDto.getEmail());
		local.setPassword(encode.encode(userDto.getPassword()));

		User user = userMapper.dtoToEntity(local);
		userRepo.save(user);

	}
	
	@Override
	public List<UserDto> findAllUsers(){
		List<User> listOfUsers=userRepo.findAll();
		List<UserDto> listOfUserDtos=new ArrayList<>();
		for(User user: listOfUsers) {
			  listOfUserDtos.add( userMapper.entityToDto(user));
		}
		
		return listOfUserDtos;
	}
	

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepo.findByEmail(username);
		if (user == null)
			throw new UsernameNotFoundException("Try with anther email");

		return new org.springframework.security.core.userdetails.User(username, user.getPassword(), new ArrayList<>());
	}
}
