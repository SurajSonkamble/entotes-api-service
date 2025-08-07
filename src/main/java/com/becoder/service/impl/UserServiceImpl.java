package com.becoder.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.becoder.dto.PasswordChangRequest;
import com.becoder.entity.User;
import com.becoder.repository.UserRepository;
import com.becoder.service.UserService;
import com.becoder.util.CommonUtils;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepository;

	@Override
	public void changePassword(PasswordChangRequest passChangRequest) {

		User loggedInUser = CommonUtils.getLoggedInUser();

		if (!passwordEncoder.matches(passChangRequest.getOldPassword(), loggedInUser.getPassword())) {

			throw new IllegalArgumentException("Old password is invalid");
		}

		String encodePassword = passwordEncoder.encode(passChangRequest.getNewPassword());

		loggedInUser.setPassword(encodePassword);

		userRepository.save(loggedInUser);

	}

}
