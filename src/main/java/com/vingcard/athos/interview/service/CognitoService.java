package com.vingcard.athos.interview.service;

import com.vingcard.athos.interview.dto.LoginTokenResponseDto;
import com.vingcard.athos.interview.enums.RoleEnum;
import com.vingcard.athos.interview.persistence.entity.User;

public interface CognitoService {

	User registerUser(String email, String password, RoleEnum role);

	LoginTokenResponseDto loginUser(String email, String password);

	void confirmEmail(String email, String confirmationCode);

	void resendConfirmationCode(String email);
}
