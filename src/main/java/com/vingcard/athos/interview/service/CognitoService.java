package com.vingcard.athos.interview.service;

import com.vingcard.athos.interview.model.dto.request.RevokeGrantRolesRequestDto;
import com.vingcard.athos.interview.model.dto.request.UserRegistrationRequestDto;
import com.vingcard.athos.interview.model.dto.response.JwtAuthenticatedUserInfo;
import com.vingcard.athos.interview.model.dto.response.ResendEmailResponseDto;
import com.vingcard.athos.interview.model.dto.response.UserLoginResponseDto;
import com.vingcard.athos.interview.model.dto.response.ValidateEmailResponseDto;
import com.vingcard.athos.interview.persistence.entity.auth.User;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

public interface CognitoService {

	User signupUser(UserRegistrationRequestDto userRegistrationRequestDto);

	UserLoginResponseDto loginUser(String email, String password);

	ValidateEmailResponseDto validateEmail(String email, String code);

	ResendEmailResponseDto resendEmailValidationCode(String email);

	User revokeUserRole(String email, RevokeGrantRolesRequestDto roles);

	User grantUserRole(String email, RevokeGrantRolesRequestDto roles);

	JwtAuthenticatedUserInfo getAuthenticatedUserInfo(Jwt jwt);

	void preRegisterReaderAndWriterUsers();

	void VerifyDefaultUser(List<User> users);

}
