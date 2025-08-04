package com.vingcard.athos.interview.exception.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request,
	                     HttpServletResponse response,
	                     AuthenticationException authException) {

		try {
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			response.setContentType("application/json");

			Map<String, Object> body = new HashMap<>();
			body.put("timestamp", LocalDateTime.now().toString());
			body.put("success", false);
			body.put("code", HttpStatus.UNAUTHORIZED.value());
			body.put("message", "Authentication required");

			OutputStream out = response.getOutputStream();
			ObjectMapper mapper = new ObjectMapper();
			mapper.writeValue(out, body);
			out.flush();

		} catch (Exception e) {
			throw new RuntimeException("Failed to handle unauthorized error", e);
		}
	}
}
