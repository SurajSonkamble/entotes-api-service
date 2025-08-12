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
import com.becoder.endpoint.AuthEndpoint;
import com.becoder.service.AuthService;
import com.becoder.util.CommonUtils;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class AuthController implements AuthEndpoint {

	@Autowired
	private AuthService authService;

	@Override
	public ResponseEntity<?> registerUser(UserRequest userDto, HttpServletRequest request) throws Exception {

		log.info("AuthController : registerUser() : Exceution Start");

		String url = CommonUtils.getUrl(request);

		boolean register = authService.register(userDto, url);

		if (!register) {

			log.info("Error : {}", "Register failed");
			return CommonUtils.createBuildResponseMessage("User Not Registered", HttpStatus.INTERNAL_SERVER_ERROR);

		}

		log.info("AuthController : registerUser() : Execution End");
		return CommonUtils.createBuildResponseMessage("User Resistration Success", HttpStatus.CREATED);

	}

	@Override
	public ResponseEntity<?> login(LoginRequest loginRequest) {

		LoginResponse loginResponse = authService.login(loginRequest);

		if (ObjectUtils.isEmpty(loginResponse)) {

			return CommonUtils.createErrorResponseMessage("invalid credentails", HttpStatus.BAD_REQUEST);
		}

		return CommonUtils.createBuildResponse(loginResponse, HttpStatus.OK);
	}

}
