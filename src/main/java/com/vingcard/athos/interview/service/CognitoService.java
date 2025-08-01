package com.vingcard.athos.interview.service;

import com.vingcard.athos.interview.dto.response.LoginTokenResponseDto;
import com.vingcard.athos.interview.enums.RoleEnum;
import com.vingcard.athos.interview.persistence.entity.User;

public interface CognitoService {

	User registerUser(String email, String password);

	User changeRoleUser(String email, RoleEnum role);

	LoginTokenResponseDto loginUser(String email, String password);

	void confirmEmail(String email, String confirmationCode);

	void resendConfirmationCode(String email);

}
