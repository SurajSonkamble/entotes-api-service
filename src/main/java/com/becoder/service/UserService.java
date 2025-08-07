package com.becoder.service;

import com.becoder.dto.LoginRequest;
import com.becoder.dto.LoginResponse;
import com.becoder.dto.UserRequest;

public interface UserService {

	boolean register(UserRequest userDto, String url) throws Exception;

	LoginResponse login(LoginRequest loginRequest);

}
