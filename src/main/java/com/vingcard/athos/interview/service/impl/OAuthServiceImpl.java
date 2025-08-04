package com.vingcard.athos.interview.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.vingcard.athos.interview.model.AuthTokens;
import com.vingcard.athos.interview.model.dto.response.JwtAuthenticatedUserInfo;
import com.vingcard.athos.interview.service.OAuthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class OAuthServiceImpl implements OAuthService {

	@Value("${spring.security.oauth2.client.registration.cognito.client-id}")
	private String clientId;

	@Value("${spring.security.oauth2.client.registration.cognito.client-secret}")
	private String clientSecret;

	@Value("${spring.security.oauth2.client.registration.cognito.redirect-uri}")
	private String redirectUri;

	@Value("${aws.cognito.revoke-token-uri}")
	private String revokeTokenUri;

	@Value("${spring.security.oauth2.client.provider.cognito.token-uri}")
	private String tokenUri;

	private final RestTemplate restTemplate = new RestTemplate();

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
	 * Extract Token from login
	 *
	 * @param code AWS Code received
	 * @return Return login Data
	 */
	public AuthTokens exchangeCodeForTokens(String code) {
		HttpHeaders headers = new org.springframework.http.HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.setBasicAuth(clientId, clientSecret);

		MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
		form.add("grant_type", "authorization_code");
		form.add("code", code);
		form.add("redirect_uri", redirectUri);

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(form, headers);

		ResponseEntity<JsonNode> response = restTemplate.postForEntity(tokenUri, request, JsonNode.class);
		JsonNode body = response.getBody();

		assert body != null;

		return new AuthTokens(
				body.get("access_token").asText(),
				body.get("id_token").asText(),
				body.get("refresh_token").asText(),
				body.get("expires_in").asInt(),
				body.get("token_type").asText()
		);
	}


	/**
	 * Revoke user Token to do logout
	 * @param token JWT Token String
	 */
	public void revokeToken(String token) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.setBasicAuth(clientId, clientSecret);

		MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
		form.add("token", token);
		form.add("token_type_hint", "refresh_token");

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(form, headers);

		restTemplate.postForEntity(revokeTokenUri, request, Void.class);
	}
}
