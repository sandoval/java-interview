package com.vingcard.athos.interview.exception.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

	private final ObjectMapper mapper = new ObjectMapper();

	@Override
	public void handle(HttpServletRequest request,
	                   HttpServletResponse response,
	                   AccessDeniedException ex) throws IOException {

		response.setStatus(HttpStatus.FORBIDDEN.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);

		Map<String, Object> body = new HashMap<>();
		body.put("timestamp", Instant.now().toString());
		body.put("status", HttpStatus.FORBIDDEN.value());
		body.put("error", "Forbidden");
		body.put("message", ex.getMessage());
		body.put("path", request.getRequestURI());

		mapper.writeValue(response.getOutputStream(), body);
	}
}

