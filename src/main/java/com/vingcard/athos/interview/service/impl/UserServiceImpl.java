package com.vingcard.athos.interview.service.impl;

import com.vingcard.athos.interview.dto.LoginTokenResponseDto;
import com.vingcard.athos.interview.persistence.entity.User;
import com.vingcard.athos.interview.requests.UserRegistrationRequest;
import com.vingcard.athos.interview.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

	private final CognitoServiceImpl cognitoService;

	@Autowired
	public UserServiceImpl(CognitoServiceImpl cognitoService) {
		this.cognitoService = cognitoService;
	}

	@Override
	public User registerUser(UserRegistrationRequest userRegistrationRequest) {
		String password = userRegistrationRequest.getPassword();
		String email = userRegistrationRequest.getEmail();
		return cognitoService.registerUser(email, password);
	}

	@Override
	public LoginTokenResponseDto loginUser(String email, String password) {
		return cognitoService.loginUser(email, password);
	}
}
