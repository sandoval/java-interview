package com.vingcard.athos.interview.service;

import com.vingcard.athos.interview.dto.request.UserRegistrationRequestDto;
import com.vingcard.athos.interview.dto.response.JwtAuthenticatedUserInfo;
import com.vingcard.athos.interview.dto.response.LoginTokenResponseDto;
import com.vingcard.athos.interview.dto.response.ResendEmailResponseDto;
import com.vingcard.athos.interview.dto.response.ValidateEmailResponseDto;
import com.vingcard.athos.interview.enums.RoleEnum;
import com.vingcard.athos.interview.persistence.entity.User;
import org.springframework.security.oauth2.jwt.Jwt;

public interface CognitoService {

	User signupUser(UserRegistrationRequestDto userRegistrationRequestDto);

	LoginTokenResponseDto loginUser(String email, String password);

	ValidateEmailResponseDto validateEmail(String email, String confirmationCode);

	ResendEmailResponseDto resendEmailValidationCode(String email);

	User revokeUserRole(String email, RoleEnum role);

	User grantUserRole(String email, RoleEnum role);

	JwtAuthenticatedUserInfo getAuthenticatedUserInfo(Jwt jwt);

}
