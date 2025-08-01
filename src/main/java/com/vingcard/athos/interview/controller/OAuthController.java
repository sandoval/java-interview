package com.vingcard.athos.interview.controller;

import com.vingcard.athos.interview.dto.request.UserLoginRequestDto;
import com.vingcard.athos.interview.dto.request.UserRegistrationRequestDto;
import com.vingcard.athos.interview.dto.response.JwtAuthenticatedUserInfo;
import com.vingcard.athos.interview.dto.response.LoginTokenResponseDto;
import com.vingcard.athos.interview.dto.response.ResendEmailResponseDto;
import com.vingcard.athos.interview.dto.response.ValidateEmailResponseDto;
import com.vingcard.athos.interview.enums.RoleEnum;
import com.vingcard.athos.interview.persistence.entity.User;
import com.vingcard.athos.interview.service.CognitoService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/oauth")
@AllArgsConstructor
public class OAuthController {

	private final CognitoService cognitoService;


	/**
	 * SignUp new User
	 * Method: POST
	 *
	 * @param userRegistrationRequestDto user registration body
	 * @return Newly user registered object
	 */
	@PostMapping(value = "/signup", consumes = {"application/json"})
	public User signupUser(@Valid @RequestBody UserRegistrationRequestDto userRegistrationRequestDto) {
		return this.cognitoService.signupUser(userRegistrationRequestDto);
	}


	/**
	 * Authenticate user
	 * Method: POST
	 *
	 * @param userLoginRequestDto Login authentication Object
	 * @return Return Object with credential Tokens
	 */
	@PostMapping("/login")
	public LoginTokenResponseDto loginUser(@Valid @RequestBody UserLoginRequestDto userLoginRequestDto) {
		return this.cognitoService.loginUser(userLoginRequestDto.email(), userLoginRequestDto.password());
	}


	/**
	 * Extracts User info from JWT Access Token
	 * Path: /api/oauth/me
	 * Method: GET
	 *
	 * @param jwt Jwt Object
	 * @return Return user info object from JWT Key
	 */
	@GetMapping("/me")
	public JwtAuthenticatedUserInfo getAuthenticatedUserInfo(@AuthenticationPrincipal Jwt jwt) {
		return this.cognitoService.getAuthenticatedUserInfo(jwt);
	}


	/**
	 * Validate new User email
	 * Path: /api/oauth/validate-email
	 * Method: POST
	 *
	 * @param email Email user
	 * @param code  Code received in email inbox
	 * @return Email validation response Object
	 */
	@PostMapping("/validate-email")
	public ValidateEmailResponseDto validateEmail(@RequestParam String email,
	                                              @RequestParam String code) {
		return this.cognitoService.validateEmail(email, code);
	}


	/**
	 * Resend Email validation code
	 * Path: /api/oauth/resend-email-validation-code
	 * Method: POST
	 *
	 * @param email User email
	 * @return Return status resend email code object
	 */
	@PostMapping("/resend-email-validation-code")
	public ResendEmailResponseDto resendEmailValidationCode(@RequestParam String email) {
		return this.cognitoService.resendEmailValidationCode(email);
	}


	/**
	 * Revoke Role to User
	 * Path: /api/oauth/revoke-user-role
	 * Method: PUT
	 *
	 * @param email User Email
	 * @param role  Role to revoke
	 * @return Return updated user
	 */
	@PutMapping("/revoke-user-role")
	public User revokeUserRole(@RequestParam String email, @RequestParam RoleEnum role) {
		return this.cognitoService.revokeUserRole(email, role);
	}


	/**
	 * Grant Role to User
	 * Path: /api/oauth/grant-user-role
	 * Method: PUT
	 *
	 * @param email User Email
	 * @param role  Role to revoke
	 * @return Return updated user
	 */
	@PutMapping("/grant-user-role")
	public User grantUserRole(@RequestParam String email, @RequestParam RoleEnum role) {
		return this.cognitoService.grantUserRole(email, role);
	}
}
