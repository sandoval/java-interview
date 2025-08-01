package com.vingcard.athos.interview.config;

import com.vingcard.athos.interview.exception.auth.CustomAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http,
	                                       AccessDeniedHandler accessDeniedHandler,
	                                       CustomAuthenticationEntryPoint customAuthenticationEntryPoint) throws Exception {

		http.authorizeHttpRequests(requests -> requests
						// Permit all in /auth/**
						.requestMatchers("/oauth/confirm-email").permitAll()
						.requestMatchers("/oauth/resend-email").permitAll()
						.requestMatchers("/oauth/login").permitAll()
						.requestMatchers("/oauth/register").permitAll()

						// OAuth block admin controls
						.requestMatchers("/oauth/me").authenticated()
						.requestMatchers("/oauth/change-role").hasRole("WRITER")

						// Permit GETs in /api/** for ROLE_WRITER or ROLE_READER
						.requestMatchers(HttpMethod.GET, "/api/**").hasAnyRole("WRITER", "READER")

						// Permit POST, PUT e DELETE only for ROLE_WRITER
						.requestMatchers(HttpMethod.POST, "/api/**").hasRole("WRITER")
						.requestMatchers(HttpMethod.PUT, "/api/**").hasRole("WRITER")
						.requestMatchers(HttpMethod.DELETE, "/api/**").hasRole("WRITER")
						.anyRequest().authenticated()
				)

				// Handle exception from AWS Cognito response
				.exceptionHandling(ex -> ex
						.authenticationEntryPoint(customAuthenticationEntryPoint)
						.accessDeniedHandler(accessDeniedHandler)
				)

				// Convert Jwt key response
				.oauth2ResourceServer((oauth2) -> oauth2
						.authenticationEntryPoint(customAuthenticationEntryPoint)
						.accessDeniedHandler(accessDeniedHandler)
						.jwt(jwt -> jwt
								.jwtAuthenticationConverter(jwtAuthenticationConverter())
						)
				);

		http.csrf(AbstractHttpConfigurer::disable);
		return http.build();
	}


	/**
	 * Convert JWT key from authentication response
	 *
	 * @return Converted jwt key
	 */
	public Converter<Jwt, AbstractAuthenticationToken> jwtAuthenticationConverter() {
		JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
		grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
		grantedAuthoritiesConverter.setAuthoritiesClaimName("cognito:groups");

		JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);

		return jwtAuthenticationConverter;
	}
}
