package com.vingcard.athos.interview.service.impl;

import com.vingcard.athos.interview.dto.LoginTokenResponseDto;
import com.vingcard.athos.interview.enums.RoleEnum;
import com.vingcard.athos.interview.persistence.entity.User;
import com.vingcard.athos.interview.persistence.repository.UserRepository;
import com.vingcard.athos.interview.requests.UserRegistrationRequest;
import com.vingcard.athos.interview.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final CognitoServiceImpl cognitoService;

	public UserServiceImpl(UserRepository userRepository, CognitoServiceImpl cognitoService) {
		this.userRepository = userRepository;
		this.cognitoService = cognitoService;
	}

	@Override
	public User registerUser(UserRegistrationRequest userRegistrationRequest) {
		String password = userRegistrationRequest.getPassword();
		String email = userRegistrationRequest.getEmail();
		RoleEnum role = userRegistrationRequest.getRole();
		return cognitoService.registerUser(email, password, role);
	}

	@Override
	public LoginTokenResponseDto loginUser(String email, String password) {
		return cognitoService.loginUser(email, password);
	}
}
