package com.becoder.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.becoder.dto.EmailRequest;

import jakarta.mail.internet.MimeMessage;

@Component
public class EmailService {

	@Autowired
	private JavaMailSender mailSender;

	@Value("${spring.mail.username}")
	private String mailFrom;

	public void sendEmail(EmailRequest emailReq) throws Exception {

		MimeMessage message = mailSender.createMimeMessage();

		MimeMessageHelper helpder = new MimeMessageHelper(message);

		helpder.setFrom(mailFrom, emailReq.getTitle());

		helpder.setTo(emailReq.getTo());

		helpder.setSubject(emailReq.getSubject());

		helpder.setText(emailReq.getMessage(), true);

		mailSender.send(message);

	}

}
