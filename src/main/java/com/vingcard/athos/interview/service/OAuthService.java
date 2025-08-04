package com.vingcard.athos.interview.service;

import com.vingcard.athos.interview.model.AuthTokens;
import com.vingcard.athos.interview.model.dto.response.JwtAuthenticatedUserInfo;
import org.springframework.security.oauth2.jwt.Jwt;

public interface OAuthService {

	JwtAuthenticatedUserInfo getAuthenticatedUserInfo(Jwt jwt);

	AuthTokens exchangeCodeForTokens(String code);

	void revokeToken(String token);
}
