package com.becoder.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.becoder.dto.PasswordChangRequest;
import com.becoder.dto.UserResponse;
import com.becoder.endpoint.UserEndpoint;
import com.becoder.entity.User;
import com.becoder.service.UserService;
import com.becoder.util.CommonUtils;

@RestController

public class UserController implements UserEndpoint {

	@Autowired
	private ModelMapper mapper;

	@Autowired
	private UserService userService;

	@Override
	public ResponseEntity<?> getProfile() {

		User loggedInUser = CommonUtils.getLoggedInUser();

		UserResponse userResponse = mapper.map(loggedInUser, UserResponse.class);

		return CommonUtils.createBuildResponse(userResponse, HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> changePassword(PasswordChangRequest passwordRequest) {

		userService.changePassword(passwordRequest);

		return CommonUtils.createBuildResponseMessage("Password changed successfully", HttpStatus.OK);

	}

}
