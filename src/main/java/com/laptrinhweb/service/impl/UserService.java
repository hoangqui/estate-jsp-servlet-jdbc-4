package com.laptrinhweb.service.impl;

import com.laptrinhweb.converter.BuildingConverter;
import com.laptrinhweb.converter.UserConverter;
import com.laptrinhweb.dto.UserDTO;
import com.laptrinhweb.entity.BuildingEntity;
import com.laptrinhweb.entity.UserEntity;
import com.laptrinhweb.service.IBuildingService;
import com.laptrinhweb.service.IUserService;

public class UserService implements IUserService{

	@Override
	public UserDTO save(UserDTO newUser) {
		UserConverter userConverter = new UserConverter();
		UserEntity buildingEntity = userConverter.converterToEntity(newUser);
		return null;
	}

}
