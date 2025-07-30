package com.becoder.service;

import com.becoder.dto.UserDto;

public interface UserService {

	boolean register(UserDto userDto, String url) throws Exception;

}
