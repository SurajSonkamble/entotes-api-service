package com.becoder.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.becoder.service.HomeService;
import com.becoder.util.CommonUtils;

@RestController
@RequestMapping("/api/v1/home")
public class HomeController {

	@Autowired
	private HomeService homeService;

	@GetMapping("/verify")
	public ResponseEntity<?> verifyUserAccount(@RequestParam Integer uid, @RequestParam String code) throws Exception {

		boolean verifyAccount = homeService.verifyAccount(uid, code);

		if (verifyAccount)

			return CommonUtils.createBuildResponseMessage("Account Verifaction Success", HttpStatus.OK);

		return CommonUtils.createErrorResponseMessage("Invalid Verifaction Link", HttpStatus.BAD_REQUEST);
	}

}
