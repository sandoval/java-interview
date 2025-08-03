package com.vingcard.athos.interview.service.impl;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.*;
import com.vingcard.athos.interview.exception.ResourceNotFoundException;
import com.vingcard.athos.interview.model.dto.request.RevokeGrantRolesRequestDto;
import com.vingcard.athos.interview.model.dto.request.UserRegistrationRequestDto;
import com.vingcard.athos.interview.model.dto.response.JwtAuthenticatedUserInfo;
import com.vingcard.athos.interview.model.dto.response.ResendEmailResponseDto;
import com.vingcard.athos.interview.model.dto.response.UserLoginResponseDto;
import com.vingcard.athos.interview.model.dto.response.ValidateEmailResponseDto;
import com.vingcard.athos.interview.model.enums.RoleEnum;
import com.vingcard.athos.interview.persistence.entity.auth.Role;
import com.vingcard.athos.interview.persistence.entity.auth.User;
import com.vingcard.athos.interview.persistence.repository.RoleRepository;
import com.vingcard.athos.interview.persistence.repository.UserRepository;
import com.vingcard.athos.interview.service.CognitoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CognitoServiceImpl implements CognitoService {

	@Value("${spring.security.oauth2.client.registration.cognito.client-id}")
	private String clientId;

	@Value("${aws.cognito.user-pool-id}")
	private String userPoolId;

	@Value("${aws.default-users.reader.email}")
	private String readerUserEmail;

	@Value("${aws.default-users.reader.password}")
	private String readerUserPassword;

	@Value("${aws.default-users.reader.phone}")
	private String readerUserPhone;

	@Value("${aws.default-users.writer.email}")
	private String writerUserEmail;

	@Value("${aws.default-users.writer.password}")
	private String writerUserPassword;

	@Value("${aws.default-users.writer.phone}")
	private String writerUserPhone;

	private final AWSCognitoIdentityProvider cognitoIdentityProvider;
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;

	@Autowired
	public CognitoServiceImpl(AWSCognitoIdentityProvider cognitoIdentityProvider,
	                          UserRepository userRepository,
	                          RoleRepository roleRepository) {
		this.cognitoIdentityProvider = cognitoIdentityProvider;
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
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
					.withUserAttributes(new AttributeType().withName("phone_number")
							.withValue(userRegistrationRequestDto.phoneNumber()))
					.withUserAttributes(new AttributeType().withName("email")
							.withValue(userRegistrationRequestDto.email()));

			cognitoIdentityProvider.signUp(signUpRequest);

			// Add Role READER to new user
			AdminAddUserToGroupRequest groupRequest = new AdminAddUserToGroupRequest()
					.withUserPoolId(userPoolId)
					.withUsername(userRegistrationRequestDto.email())
					.withGroupName(RoleEnum.READER.name()); // New users ever starts with READER Role
			cognitoIdentityProvider.adminAddUserToGroup(groupRequest);

			// Get READER Roles from database
			List<Role> readerRoles = roleRepository.findByRole(RoleEnum.READER);

			// Register user on database
			User registeredUser = new User();
			registeredUser.setEmail(userRegistrationRequestDto.email());
			registeredUser.setRoles(readerRoles);
			registeredUser.setPhoneNumber(userRegistrationRequestDto.phoneNumber());
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
	 * @param roles New user Role list
	 * @return Updated user Object
	 */
	public User grantUserRole(String email, RevokeGrantRolesRequestDto roles) {
		try {
			User user = userRepository.findByEmailAndVerified(email, true)
					.orElseThrow(() -> new ResourceNotFoundException(String.format("User with email %s not found", email)));

			for (Role role : roles.roles()) {
				cognitoIdentityProvider.adminAddUserToGroup(new AdminAddUserToGroupRequest()
						.withUserPoolId(userPoolId)
						.withUsername(user.getEmail())
						.withGroupName(role.getRole().name()));

				Role newRole = roleRepository.findByRole(role.getRole()).getFirst();
				user.getRoles().add(newRole);
			}

			// If Exists update database
			user.setUpdateAt(LocalDateTime.now());
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
	 * Add Default user to application
	 */
	@Override
	public void preRegisterReaderAndWriterUsers() {

		if (!userExists(readerUserEmail) && !userExists(writerUserEmail)) {
			// Reader user DTO
			UserRegistrationRequestDto readerUser = new UserRegistrationRequestDto(
					readerUserEmail,
					readerUserPassword,
					readerUserPhone
			);

			// Writer user DTO
			UserRegistrationRequestDto writerUser = new UserRegistrationRequestDto(
					writerUserEmail,
					writerUserPassword,
					writerUserPhone
			);

			User readerUserCreated = this.signupUser(readerUser);
			User writerUserCreated = this.signupUser(writerUser);

			List<User> userList = new ArrayList<>();
			userList.add(readerUserCreated);
			userList.add(writerUserCreated);

			VerifyDefaultUser(userList);
		}
	}


	/**
	 * User Exists on AWS
	 *
	 * @param email User email
	 * @return boolean
	 */
	private boolean userExists(String email) {
		AdminGetUserRequest getUserRequest = new AdminGetUserRequest()
				.withUserPoolId(userPoolId)
				.withUsername(email);
		try {
			cognitoIdentityProvider.adminGetUser(getUserRequest);
			return true;
		} catch (UserNotFoundException e) {
			return false;
		}
	}


	/**
	 *
	 */
	@Override
	public void VerifyDefaultUser(List<User> users) {
		for (User user : users) {
			AdminConfirmSignUpRequest confirmRequest = new AdminConfirmSignUpRequest()
					.withUserPoolId(userPoolId)
					.withUsername(user.getEmail());
			cognitoIdentityProvider.adminConfirmSignUp(confirmRequest);

			User userVerified = userRepository.findByEmail(user.getEmail())
					.orElseThrow(() ->
							new ResourceNotFoundException(String
									.format("User with email %s not found", user.getEmail())));

			userVerified.setUpdateAt(LocalDateTime.now());
			userVerified.setEnabled(true);
			userVerified.setVerified(true);

			userRepository.save(userVerified);

			// Grant role to Writer user
			if (user.getEmail().equals(writerUserEmail)) {
				AdminAddUserToGroupRequest groupRequest = new AdminAddUserToGroupRequest()
						.withUserPoolId(userPoolId)
						.withUsername(user.getEmail())
						.withGroupName(RoleEnum.WRITER.name());
				cognitoIdentityProvider.adminAddUserToGroup(groupRequest);

				List<Role> writeRole = roleRepository.findByRole(RoleEnum.WRITER);
				grantUserRole(user.getEmail(), new RevokeGrantRolesRequestDto(writeRole));
			}
		}

	}


	/**
	 * Remove user to Role Group
	 *
	 * @param email User email
	 * @param roles New user Role list
	 * @return Updated user Object
	 */
	public User revokeUserRole(String email, RevokeGrantRolesRequestDto roles) {
		try {
			User user = userRepository.findByEmailAndVerified(email, true)
					.orElseThrow(() ->
							new ResourceNotFoundException(String.format("User with email %s not found", email)));

			for (Role role : roles.roles()) {
				Role revokedRole = roleRepository.findByRole(role.getRole()).getFirst();
				user.getRoles().remove(revokedRole);

				cognitoIdentityProvider.adminRemoveUserFromGroup(new AdminRemoveUserFromGroupRequest()
						.withUserPoolId(userPoolId)
						.withUsername(email)
						.withGroupName(role.getRole().name()));
			}

			// Set update at
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
	public UserLoginResponseDto loginUser(String email, String password) {

		// Check user exists
		userRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException(String.format("User with email %s not found", email)));


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

			return new UserLoginResponseDto(
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
	 * @param email Email to confirm
	 * @param code  Code received by email
	 * @return Validation email response
	 */
	public ValidateEmailResponseDto validateEmail(String email, String code) {
		try {
			ConfirmSignUpRequest confirmRequest = new ConfirmSignUpRequest()
					.withClientId(clientId)
					.withUsername(email)
					.withConfirmationCode(code);

			// Get existing user
			Optional<User> user = this.userRepository.findByEmail(email);

			if (user.isEmpty()) {
				throw new ResourceNotFoundException(String.format("User with email %s not found", email));
			}

			user.get().setVerified(true);
			user.get().setUpdateAt(LocalDateTime.now());

			this.userRepository.save(user.get());
			cognitoIdentityProvider.confirmSignUp(confirmRequest);

			return new ValidateEmailResponseDto(true, email, "Email successfully validated.");

		} catch (CodeMismatchException e) {
			throw new RuntimeException(String.format("%s is not valid confirmation code.", code));
		} catch (ExpiredCodeException e) {
			throw new RuntimeException("Confirmation code has expired.");
		} catch (UserNotFoundException e) {
			// User not found on AWS
			throw new ResourceNotFoundException(String.format("User with email %s not found", email));
		} catch (ResourceNotFoundException e) {
			throw new ResourceNotFoundException(String.format("User with email %s not found", email));
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
			throw new ResourceNotFoundException(String.format("User with email %s not found", email));
		} catch (InvalidParameterException e) {
			throw new RuntimeException(e);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
}
