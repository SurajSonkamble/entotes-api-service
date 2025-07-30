package com.becoder.service;

public interface HomeService {
	
	public boolean verifyAccount(Integer userId, String verificationCode) throws Exception;

}
