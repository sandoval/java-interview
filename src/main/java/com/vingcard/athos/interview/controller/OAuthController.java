package com.vingcard.athos.interview.controller;

import com.vingcard.athos.interview.dto.LoginTokenResponseDto;
import com.vingcard.athos.interview.dto.ResendEmailResponseDto;
import com.vingcard.athos.interview.dto.ValidateEmailResponseDto;
import com.vingcard.athos.interview.enums.RoleEnum;
import com.vingcard.athos.interview.persistence.entity.User;
import com.vingcard.athos.interview.requests.UserLoginRequest;
import com.vingcard.athos.interview.requests.UserRegistrationRequest;
import com.vingcard.athos.interview.service.CognitoService;
import com.vingcard.athos.interview.service.impl.UserServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/oauth")
public class OAuthController {

	private final UserServiceImpl userService;
	private final CognitoService cognitoService;

	@Autowired
	public OAuthController(UserServiceImpl userService,
	                       CognitoService cognitoService) {
		this.userService = userService;
		this.cognitoService = cognitoService;
	}

	@PostMapping(value = "/register", consumes = {"application/json"})
	public ResponseEntity<User> registerUser(@Valid @RequestBody UserRegistrationRequest userRegistrationRequest) {
		User registeredUser = this.userService.registerUser(userRegistrationRequest);

		if (registeredUser != null) {
			return ResponseEntity.ok(registeredUser);
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}

	@PostMapping("/login")
	public ResponseEntity<LoginTokenResponseDto> loginUser(@Valid @RequestBody UserLoginRequest userLoginRequest) {
		LoginTokenResponseDto tokenResponseDto = userService.loginUser(userLoginRequest.getEmail(),
				userLoginRequest.getPassword());

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
					"info.email_confirmed"));
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
					"{info.email_resent}"));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ResendEmailResponseDto(false, email, "{info.email_resent}"));
		}
	}

	@GetMapping("/change-role")
	public User changeRoleUser(@RequestParam String email, @RequestParam RoleEnum role) {
		return this.cognitoService.changeRoleUser(email, role);
	}
}
