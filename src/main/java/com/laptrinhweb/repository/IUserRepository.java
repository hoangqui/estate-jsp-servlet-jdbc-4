package com.laptrinhweb.repository;

import com.laptrinhweb.entity.UserEntity;

public interface IUserRepository extends IGenericJDBC<UserEntity> {
	Long insert(UserEntity userEntity);
}
