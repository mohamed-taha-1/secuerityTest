package com.entities.user;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;




@Mapper(componentModel = "spring")
public interface UserMapper {
	UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
	
	
	UserDto entityToDto(User entity);

	@Mapping(target = "id" , ignore = true)
	User dtoToEntity(UserDto user);

}
