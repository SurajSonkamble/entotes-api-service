package com.becoder.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.becoder.dto.PswdResetRequest;
import com.becoder.service.HomeService;
import com.becoder.service.UserService;
import com.becoder.util.CommonUtils;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/v1/home")
public class HomeController {

	@Autowired
	private HomeService homeService;

	@Autowired
	private UserService userService;

	@GetMapping("/verify")
	public ResponseEntity<?> verifyUserAccount(@RequestParam Integer uid, @RequestParam String code) throws Exception {

		boolean verifyAccount = homeService.verifyAccount(uid, code);

		if (verifyAccount)

			return CommonUtils.createBuildResponseMessage("Account Verifaction Success", HttpStatus.OK);

		return CommonUtils.createErrorResponseMessage("Invalid Verifaction Link", HttpStatus.BAD_REQUEST);
	}

	@GetMapping("/send-email-reset")
	public ResponseEntity<?> sendEmailForPasswordReset(@RequestParam String email, HttpServletRequest request)
			throws Exception {

		userService.sendEmailPasswordReset(email, request);

		return CommonUtils.createBuildResponseMessage("Email Send Success !! Check Email Reset Password",
				HttpStatus.OK);

	}

	@GetMapping("/verify-pswd-link")
	public ResponseEntity<?> verifyPasswordResetLink(@RequestParam Integer uid, @RequestParam String code)
			throws Exception {

		userService.verifyPswdResetLink(uid, code);

		return CommonUtils.createBuildResponseMessage("Verification Success", HttpStatus.OK);
	}

	@PostMapping("/reset-pswd")
	public ResponseEntity<?> resetPassword(@RequestBody PswdResetRequest pswdResetRequest) throws Exception {

		userService.resetPassword(pswdResetRequest);

		return  CommonUtils.createBuildResponseMessage("Psssword Reset Success", HttpStatus.OK);
	}

}
