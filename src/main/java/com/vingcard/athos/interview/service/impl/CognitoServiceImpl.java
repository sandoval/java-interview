package com.vingcard.athos.interview.service.impl;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.*;
import com.vingcard.athos.interview.dto.request.UserRegistrationRequestDto;
import com.vingcard.athos.interview.dto.response.JwtAuthenticatedUserInfo;
import com.vingcard.athos.interview.dto.response.LoginTokenResponseDto;
import com.vingcard.athos.interview.dto.response.ResendEmailResponseDto;
import com.vingcard.athos.interview.dto.response.ValidateEmailResponseDto;
import com.vingcard.athos.interview.enums.RoleEnum;
import com.vingcard.athos.interview.exception.NotFoundExceptionResponse;
import com.vingcard.athos.interview.persistence.entity.User;
import com.vingcard.athos.interview.persistence.repository.UserRepository;
import com.vingcard.athos.interview.service.CognitoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class CognitoServiceImpl implements CognitoService {

	@Value("${spring.security.oauth2.client.registration.cognito.client-id}")
	private String clientId;

	@Value("${aws.cognito.user-pool-id}")
	private String userPoolId;

	private final AWSCognitoIdentityProvider cognitoIdentityProvider;
	private final UserRepository userRepository;

	@Autowired
	public CognitoServiceImpl(AWSCognitoIdentityProvider cognitoIdentityProvider, UserRepository userRepository) {
		this.cognitoIdentityProvider = cognitoIdentityProvider;
		this.userRepository = userRepository;
	}


	/**
	 * Create new user account from AWS Cognito and add in database
	 *
	 * @param userRegistrationRequestDto Registration user Object
	 * @return Newly created User Object
	 */
	public User signupUser(UserRegistrationRequestDto userRegistrationRequestDto) {
		try {
			SignUpRequest signUpRequest = new SignUpRequest()
					.withClientId(clientId)
					.withUsername(userRegistrationRequestDto.email())
					.withPassword(userRegistrationRequestDto.password())
					.withUserAttributes(new AttributeType().withName("email")
							.withValue(userRegistrationRequestDto.email()));

			cognitoIdentityProvider.signUp(signUpRequest);

			AdminAddUserToGroupRequest groupRequest = new AdminAddUserToGroupRequest()
					.withUserPoolId(userPoolId)
					.withUsername(userRegistrationRequestDto.email())
					.withGroupName(RoleEnum.READER.name()); // New users ever starts with READER Role
			cognitoIdentityProvider.adminAddUserToGroup(groupRequest);

			// Register user on database
			User registeredUser = new User();
			registeredUser.setEmail(userRegistrationRequestDto.email());
			registeredUser.setRole(RoleEnum.READER);
			registeredUser.setPassword(userRegistrationRequestDto.password());

			return userRepository.save(registeredUser);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}


	/**
	 * Add user to Role Group
	 *
	 * @param email User email
	 * @param role  New user Role
	 * @return Updated user Object
	 */
	public User grantUserRole(String email, RoleEnum role) {
		try {
			cognitoIdentityProvider.adminAddUserToGroup(new AdminAddUserToGroupRequest()
					.withUserPoolId(userPoolId)
					.withUsername(email)
					.withGroupName(role.name()));

			User user = userRepository.findByEmailAndVerified(email, true);

			if (user == null) {
				throw new NotFoundExceptionResponse(String.format("User with email %s not found", email));
			}

			// If Exists update database
			user.setUpdateAt(LocalDateTime.now());
			user.setRole(role);

			return userRepository.save(user);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}


	/**
	 * Extracts User info from JWT Access Token
	 *
	 * @param jwt Jwt Object
	 * @return Return user info object from JWT Key
	 */
	@Override
	public JwtAuthenticatedUserInfo getAuthenticatedUserInfo(Jwt jwt) {
		return new JwtAuthenticatedUserInfo(
				jwt.getSubject(),
				jwt.getIssuedAt(),
				jwt.getExpiresAt(),
				jwt.getClaimAsStringList("cognito:groups")
		);
	}


	/**
	 * Remove user to Role Group
	 *
	 * @param email User email
	 * @param role  New user Role
	 * @return Updated user Object
	 */
	public User revokeUserRole(String email, RoleEnum role) {
		try {
			cognitoIdentityProvider.adminRemoveUserFromGroup(new AdminRemoveUserFromGroupRequest()
					.withUserPoolId(userPoolId)
					.withUsername(email)
					.withGroupName(role.name()));

			User user = userRepository.findByEmailAndVerified(email, true);

			if (user == null) {
				throw new NotFoundExceptionResponse(String.format("User with email %s not found", email));
			}

			// If Exists update database
			user.setRole(role);
			user.setUpdateAt(LocalDateTime.now());

			return userRepository.save(user);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}


	/**
	 * Login user with AWS Cognito
	 *
	 * @param email    User email
	 * @param password User password
	 * @return Access token and refresh token from AWS Cognito
	 */
	public LoginTokenResponseDto loginUser(String email, String password) {
		User user = userRepository.findByEmail(email);

		if (user == null) {
			throw new NotFoundExceptionResponse(String.format("User with email %s not found", email));
		}

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
			String tokenType = authResponse.getTokenType();

			return new LoginTokenResponseDto(
					tokenType,
					accessToken,
					refreshToken,
					expiresIn
			);

		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}


	/**
	 * Confirm new email user using code received by email
	 *
	 * @param email            Email to confirm
	 * @param confirmationCode Code received by email
	 * @return Validation email response
	 */
	public ValidateEmailResponseDto validateEmail(String email, String confirmationCode) {
		try {
			ConfirmSignUpRequest confirmRequest = new ConfirmSignUpRequest()
					.withClientId(clientId)
					.withUsername(email)
					.withConfirmationCode(confirmationCode);

			User user = this.userRepository.findByEmail(email);

			// User not found on Database
			if (user == null) {
				throw new NotFoundExceptionResponse(String.format("User with email %s not found", email));
			}

			user.setVerified(true);
			user.setUpdateAt(LocalDateTime.now());

			this.userRepository.save(user);
			cognitoIdentityProvider.confirmSignUp(confirmRequest);

			return new ValidateEmailResponseDto(true,
					email,
					"Email successfully validated.");

		} catch (CodeMismatchException e) {
			throw new RuntimeException(String.format("%s is not valid confirmation code.", confirmationCode));
		} catch (ExpiredCodeException e) {
			throw new RuntimeException("Confirmation code has expired.");
		} catch (UserNotFoundException e) {
			// User not found on AWS
			throw new NotFoundExceptionResponse(String.format("User with email %s not found", email));
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}


	/**
	 * Resend confirmation code to user email
	 *
	 * @param email Email to send confirmation code
	 */
	public ResendEmailResponseDto resendEmailValidationCode(String email) {
		try {
			ResendConfirmationCodeRequest resendRequest = new ResendConfirmationCodeRequest()
					.withClientId(clientId)
					.withUsername(email);

			cognitoIdentityProvider.resendConfirmationCode(resendRequest);

			return new ResendEmailResponseDto(200,
					true,
					email,
					"Verification email successfully resent.");
		} catch (UserNotFoundException e) {
			throw new NotFoundExceptionResponse(String.format("User with email %s not found", email));
		} catch (InvalidParameterException e) {
			throw new RuntimeException(e);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
}
