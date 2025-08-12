package com.becoder.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.becoder.dto.PswdResetRequest;
import com.becoder.endpoint.HomeEndpoint;
import com.becoder.service.HomeService;
import com.becoder.service.UserService;
import com.becoder.util.CommonUtils;

import jakarta.servlet.http.HttpServletRequest;

@RestController

public class HomeController implements HomeEndpoint {

	Logger log = LoggerFactory.getLogger(HomeController.class);

	@Autowired
	private HomeService homeService;

	@Autowired
	private UserService userService;

	@Override
	public ResponseEntity<?> verifyUserAccount(Integer uid, @RequestParam String code) throws Exception {

		log.info("HomeController: verifyUserAccount(): Exeution Start");

		boolean verifyAccount = homeService.verifyAccount(uid, code);

		if (!verifyAccount)

			return CommonUtils.createErrorResponseMessage("Invalid Verifaction Link", HttpStatus.BAD_REQUEST);

		log.info("HomeController: verifyUserAccount() : Exeution End");
		return CommonUtils.createBuildResponseMessage("Account Verifaction Success", HttpStatus.OK);

	}

	@Override
	public ResponseEntity<?> sendEmailForPasswordReset(String email, HttpServletRequest request) throws Exception {

		userService.sendEmailPasswordReset(email, request);

		return CommonUtils.createBuildResponseMessage("Email Send Success !! Check Email Reset Password",
				HttpStatus.OK);

	}

	@Override
	public ResponseEntity<?> verifyPasswordResetLink(Integer uid, @RequestParam String code) throws Exception {

		userService.verifyPswdResetLink(uid, code);

		return CommonUtils.createBuildResponseMessage("Verification Success", HttpStatus.OK);
	}

	@Override
	public ResponseEntity<?> resetPassword(PswdResetRequest pswdResetRequest) throws Exception {

		userService.resetPassword(pswdResetRequest);

		return CommonUtils.createBuildResponseMessage("Psssword Reset Success", HttpStatus.OK);
	}

}
