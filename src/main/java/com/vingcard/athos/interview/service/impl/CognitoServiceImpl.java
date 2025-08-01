package com.vingcard.athos.interview.service.impl;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.*;
import com.vingcard.athos.interview.dto.LoginTokenResponseDto;
import com.vingcard.athos.interview.enums.RoleEnum;
import com.vingcard.athos.interview.exception.NotFoundExceptionResponse;
import com.vingcard.athos.interview.persistence.entity.User;
import com.vingcard.athos.interview.persistence.repository.UserRepository;
import com.vingcard.athos.interview.service.CognitoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class CognitoServiceImpl implements CognitoService {

	@Value("${spring.security.oauth2.client.registration.cognito.client-id}")
	private String clientId;

	@Value("${spring.security.oauth2.client.registration.cognito.client-secret}")
	private String clientSecret;

	@Value("${spring.security.oauth2.client.registration.cognito.scope}")
	private String scope;

	@Value("${spring.security.oauth2.client.provider.cognito.issuer-uri}")
	private String issuerUri;

	@Value("${aws.cognito.user-pool-id}")
	private String userPoolId;

	private final AWSCognitoIdentityProvider cognitoIdentityProvider;
	private final UserRepository userRepository;

	public CognitoServiceImpl(UserRepository userRepository, AWSCognitoIdentityProvider cognitoIdentityProvider) {
		this.userRepository = userRepository;
		this.cognitoIdentityProvider = cognitoIdentityProvider;
	}

	public User registerUser(String email, String password, RoleEnum role) {

		SignUpRequest signUpRequest = new SignUpRequest()
				.withClientId(clientId)
				.withUsername(email)
				.withPassword(password)
				.withUserAttributes(new AttributeType().withName("email").withValue(email));

		try {
			SignUpResult signUpResult = cognitoIdentityProvider.signUp(signUpRequest);

			AdminAddUserToGroupRequest groupRequest = new AdminAddUserToGroupRequest()
					.withUserPoolId(userPoolId)
					.withUsername(email)
					.withGroupName(role.name());
			cognitoIdentityProvider.adminAddUserToGroup(groupRequest);

			User registeredUser = new User();
			registeredUser.setEmail(email);
			registeredUser.setRole(role);
			registeredUser.setPassword(password);

			return userRepository.save(registeredUser);
		} catch (Exception e) {
			throw new RuntimeException("User registration failed: " + e.getMessage(), e);
		}
	}

	public LoginTokenResponseDto loginUser(String email, String password) {

		InitiateAuthRequest authRequest = new InitiateAuthRequest()
				.withAuthFlow("USER_PASSWORD_AUTH")
				.withClientId(clientId)
				.withAuthParameters(Map.of("USERNAME", email, "PASSWORD", password));

		try {
			InitiateAuthResult authResult = cognitoIdentityProvider.initiateAuth(authRequest);
			System.out.println(authResult);
			AuthenticationResultType authResponse = authResult.getAuthenticationResult();

			String accessToken = authResponse.getAccessToken();
			String refreshToken = authResponse.getRefreshToken();
			Integer expiresIn = authResponse.getExpiresIn();

			return new LoginTokenResponseDto(accessToken,
					refreshToken,
					expiresIn);

		} catch (Exception e) {
			throw new RuntimeException("User login failed: " + e.getMessage(), e);
		}
	}

	public void confirmEmail(String email, String confirmationCode) {
		try {
			ConfirmSignUpRequest confirmRequest = new ConfirmSignUpRequest()
					.withClientId(clientId)
					.withUsername(email)
					.withConfirmationCode(confirmationCode);

			User user = this.userRepository.findByEmailAndVerified(email, false);
			user.setVerified(true);
			user.setUpdateAt(LocalDateTime.now());

			this.userRepository.save(user);

			cognitoIdentityProvider.confirmSignUp(confirmRequest);
		} catch (CodeMismatchException e) {
			throw new RuntimeException("Invalid confirmation code.", e);
		} catch (ExpiredCodeException e) {
			throw new RuntimeException("Confirmation code has expired.", e);
		} catch (UserNotFoundException e) {
			throw new NotFoundExceptionResponse("User not found.");
		} catch (Exception e) {
			throw new RuntimeException("Error confirming email: " + e.getMessage(), e);
		}
	}

	public void resendConfirmationCode(String email) {
		try {
			ResendConfirmationCodeRequest resendRequest = new ResendConfirmationCodeRequest()
					.withClientId(clientId)
					.withUsername(email);

			cognitoIdentityProvider.resendConfirmationCode(resendRequest);
		} catch (UserNotFoundException e) {
			throw new RuntimeException("User not found.", e);
		} catch (InvalidParameterException e) {
			throw new RuntimeException("User is already confirmed or cannot resend code.", e);
		} catch (Exception e) {
			throw new RuntimeException("Error resending confirmation code: " + e.getMessage(), e);
		}
	}
}
