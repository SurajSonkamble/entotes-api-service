package com.becoder.service.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.becoder.dto.EmailRequest;
import com.becoder.dto.PasswordChangRequest;
import com.becoder.dto.PswdResetRequest;
import com.becoder.entity.User;
import com.becoder.exception.ResourceNotFoundException;
import com.becoder.repository.UserRepository;
import com.becoder.service.UserService;
import com.becoder.util.CommonUtils;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private EmailService emailService;

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

	@Override
	public void sendEmailPasswordReset(String email, HttpServletRequest request) throws Exception {

		User user = userRepository.findByEmail(email);

		if (ObjectUtils.isEmpty(user)) {

			throw new ResourceNotFoundException("Invalid Email Address");
		}

		// generate unique password reset token

		String passwordResetToken = UUID.randomUUID().toString();

		user.getStatus().setPasswordResetToken(passwordResetToken);

		User updateUser = userRepository.save(user);

		String url = CommonUtils.getUrl(request);

		sendEmailRequest(updateUser, url);

	}

	private void sendEmailRequest(User user, String url) throws Exception {

		String message = "Hi,<b>  [[username]]  </b>" + "<br><p> You have Requested to reset password.</p><br>"
				+ "<p>Click the link below to change your password:</p>"
				+ "<p><a href=[[url]]>Change my password</a></p>"
				+ "<p>Ignore this email if you do remember your password, "
				+ "or you have not made the request.</p><br>" + "Thanks,<br>Enotes.com";

		message = message.replace("[[username]]", user.getFirstName());
		message = message.replace("[[url]]", url + "/api/v1/home/verify-pswd-link?uid=" + user.getId() + "&&code="
				+ user.getStatus().getPasswordResetToken());

		EmailRequest emailRequest = EmailRequest.builder().to(user.getEmail()).title("Password Reset")
				.subject("Password Reset link").message(message).build();

		// send password reset email to user
		emailService.sendEmail(emailRequest);
	}

	@Override
	public void verifyPswdResetLink(Integer uid, String code) throws Exception {

		User user = userRepository.findById(uid).orElseThrow(() -> new ResourceNotFoundException("User id Invalid"));

		verifyPasswordResetCode(user.getStatus().getPasswordResetToken(), code);

	}

	public void verifyPasswordResetCode(String existToken, String reqToken) throws Exception {


		// request token not null
		if (StringUtils.hasText(reqToken)) {
			// password already reset
			if (!StringUtils.hasText(existToken)) {
				throw new IllegalArgumentException("Already Password reset");
			}
			// user req token changes
			if (!existToken.equals(reqToken)) {
				throw new IllegalArgumentException("invalid url");
			}
		} else {
			throw new IllegalArgumentException("invalid token");
		}
	}

	@Override
	public void resetPassword(PswdResetRequest pswdResetRequest) throws Exception {

		User user = userRepository.findById(pswdResetRequest.getUid())
				.orElseThrow(() -> new ResourceNotFoundException("Invalid User"));

		String encodePassword = passwordEncoder.encode(pswdResetRequest.getNewPassword());

		user.setPassword(encodePassword);

		user.getStatus().setPasswordResetToken(null);

		userRepository.save(user);

	}

}
