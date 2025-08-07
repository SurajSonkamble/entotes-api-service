package com.becoder.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.becoder.dto.LoginRequest;
import com.becoder.dto.LoginResponse;
import com.becoder.dto.UserRequest;
import com.becoder.service.AuthService;
import com.becoder.util.CommonUtils;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

	@Autowired
	private AuthService authService;

	@PostMapping("/")
	public ResponseEntity<?> registerUser(@RequestBody UserRequest userDto, HttpServletRequest request) throws Exception {

		String url = CommonUtils.getUrl(request);

		boolean register = authService.register(userDto, url);

		if (register) {

			return CommonUtils.createBuildResponseMessage("User Resistration Success", HttpStatus.CREATED);
		}

		return CommonUtils.createBuildResponseMessage("User Not Registered", HttpStatus.INTERNAL_SERVER_ERROR);
	}

	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {

		LoginResponse loginResponse = authService.login(loginRequest);

		if (ObjectUtils.isEmpty(loginResponse)) {

			return CommonUtils.createErrorResponseMessage("invalid credentails", HttpStatus.BAD_REQUEST);
		}

		return CommonUtils.createBuildResponse(loginResponse, HttpStatus.OK);
	}

}
