package com.vingcard.athos.interview.service.impl;

import com.vingcard.athos.interview.dto.response.LoginTokenResponseDto;
import com.vingcard.athos.interview.persistence.entity.User;
import com.vingcard.athos.interview.dto.request.UserRegistrationRequestDto;
import com.vingcard.athos.interview.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

	private final CognitoServiceImpl cognitoService;


	/**
	 * Register user in AWS Cognito and local database
	 *
	 * @param userRegistrationRequestDto Register User Request Body Content
	 * @return Newly registered object user
	 */
	@Override
	public User registerUser(UserRegistrationRequestDto userRegistrationRequestDto) {
		String password = userRegistrationRequestDto.password();
		String email = userRegistrationRequestDto.email();

		return cognitoService.registerUser(email, password);
	}


	/**
	 * Login user with AWS Cognito account
	 *
	 * @param email    AWS Cognito Email User
	 * @param password AWS Cognito Email Password
	 * @return LoginTokenResponseDto
	 */
	@Override
	public LoginTokenResponseDto loginUser(String email, String password) {
		return cognitoService.loginUser(email, password);
	}
}
