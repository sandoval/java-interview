package com.vingcard.athos.interview.controller;

import com.vingcard.athos.interview.dto.request.UserLoginRequestDto;
import com.vingcard.athos.interview.dto.request.UserRegistrationRequestDto;
import com.vingcard.athos.interview.dto.response.LoginTokenResponseDto;
import com.vingcard.athos.interview.dto.response.ResendEmailResponseDto;
import com.vingcard.athos.interview.dto.response.ValidateEmailResponseDto;
import com.vingcard.athos.interview.enums.RoleEnum;
import com.vingcard.athos.interview.persistence.entity.User;
import com.vingcard.athos.interview.service.CognitoService;
import com.vingcard.athos.interview.service.impl.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/oauth")
@AllArgsConstructor
public class OAuthController {

	private final UserServiceImpl userService;
	private final CognitoService cognitoService;

	@PostMapping(value = "/register", consumes = {"application/json"})
	public ResponseEntity<User> registerUser(@Valid @RequestBody UserRegistrationRequestDto userRegistrationRequestDto) {
		User registeredUser = this.userService.registerUser(userRegistrationRequestDto);

		if (registeredUser != null) {
			return ResponseEntity.ok(registeredUser);
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PostMapping("/login")
	public ResponseEntity<LoginTokenResponseDto> loginUser(@Valid @RequestBody UserLoginRequestDto userLoginRequestDto) {
		LoginTokenResponseDto tokenResponseDto = userService.loginUser(
				userLoginRequestDto.email(),
				userLoginRequestDto.password()
		);

		if (tokenResponseDto != null) {
			return ResponseEntity.ok(tokenResponseDto);
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
	}

	@GetMapping("/me")
	public ResponseEntity<Map<String, Object>> getAuthenticatedUserInfo(@AuthenticationPrincipal Jwt jwt) {
		Map<String, Object> userInfo = new HashMap<>();

		userInfo.put("sub", jwt.getSubject());
		userInfo.put("issuedAt", jwt.getIssuedAt());
		userInfo.put("expiresAt", jwt.getExpiresAt());
		userInfo.put("roles", jwt.getClaimAsStringList("cognito:groups"));

		return ResponseEntity.ok(userInfo);
	}

	@PostMapping("/confirm-email")
	public ResponseEntity<ValidateEmailResponseDto> confirmEmail(@RequestParam String email,
	                                                             @RequestParam String code) {
		try {
			this.cognitoService.confirmEmail(email, code);
			return ResponseEntity.ok(new ValidateEmailResponseDto(true,
					email,
					"Email successfully confirmed."));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ValidateEmailResponseDto(false, email, e.getMessage()));
		}
	}

	@PostMapping("/resend-email")
	public ResponseEntity<ResendEmailResponseDto> resendEmailVerification(@RequestParam String email) {
		try {
			this.cognitoService.resendConfirmationCode(email);
			return ResponseEntity.ok(new ResendEmailResponseDto(true,
					email,
					"Verification email successfully resent."));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ResendEmailResponseDto(false, email, "Verification email resent failed."));
		}
	}


	/**
	 * Revoke Role to User
	 * Path: /api/oauth/revoke-user-role
	 *
	 * @param email User Email
	 * @param role  Role to revoke
	 * @return Return updated user
	 */
	@GetMapping("/revoke-user-role")
	public User revokeUserRole(@RequestParam String email, @RequestParam RoleEnum role) {
		return this.cognitoService.revokeUserRole(email, role);
	}


	/**
	 * Grant Role to User
	 * Path: /api/oauth/grant-user-role
	 *
	 * @param email User Email
	 * @param role  Role to revoke
	 * @return Return updated user
	 */
	@GetMapping("/grant-user-role")
	public User grantUserRole(@RequestParam String email, @RequestParam RoleEnum role) {
		return this.cognitoService.grantUserRole(email, role);
	}
}
