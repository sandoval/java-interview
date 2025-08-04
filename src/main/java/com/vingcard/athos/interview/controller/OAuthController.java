package com.vingcard.athos.interview.controller;

import com.vingcard.athos.interview.model.AuthTokens;
import com.vingcard.athos.interview.model.dto.response.JwtAuthenticatedUserInfo;
import com.vingcard.athos.interview.service.OAuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/oauth")
@AllArgsConstructor
public class OAuthController {

	private final OAuthService oAuthService;
	private final InMemoryClientRegistrationRepository clientRegistrationRepository;

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
		return this.oAuthService.getAuthenticatedUserInfo(jwt);
	}


	/**
	 * Autorize user to acess endpoints
	 *
	 * @param response
	 * @throws IOException
	 */
	@GetMapping("/authorize")
	public void authorize(HttpServletResponse response) throws IOException {
		ClientRegistration registration = clientRegistrationRepository.findByRegistrationId("cognito");

		String redirectUri = UriComponentsBuilder
				.fromUriString(registration.getProviderDetails().getAuthorizationUri())
				.queryParam("response_type", "code")
				.queryParam("client_id", registration.getClientId())
				.queryParam("redirect_uri", registration.getRedirectUri())
				.queryParam("scope", String.join("+", registration.getScopes()))
				.build()
				.toUriString();

		response.sendRedirect(redirectUri);
	}

	/**
	 * Callback AWS Cognito Claims
	 *
	 * @param code Code received by AWS
	 * @return Status code for authentication
	 */
	@GetMapping("/callback")
	public ResponseEntity<?> callback(@RequestParam String code) {
		AuthTokens tokens = oAuthService.exchangeCodeForTokens(code);
		System.out.println("tokens: " + tokens);
		return ResponseEntity.ok(tokens);
	}


	/**
	 * @param authorizationHeader AccessToken
	 * @return Return status code
	 */
	@PostMapping("/revoke-token")
	public ResponseEntity<Void> revokeToken(
			@RequestHeader("Authorization") String authorizationHeader,
			@RequestBody Map<String, String> body
	) {
		if (!authorizationHeader.startsWith("Bearer ")) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

		String refreshToken = body.get("refreshToken");
		if (refreshToken == null || refreshToken.isBlank()) {
			return ResponseEntity.badRequest().build();
		}

		oAuthService.revokeToken(refreshToken);
		return ResponseEntity.noContent().build();
	}
}
