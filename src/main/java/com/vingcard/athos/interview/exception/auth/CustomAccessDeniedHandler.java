package com.vingcard.athos.interview.exception.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

	@Override
	public void handle(HttpServletRequest request,
	                   HttpServletResponse response,
	                   AccessDeniedException accessDeniedException) throws IOException {

		response.setStatus(HttpStatus.FORBIDDEN.value());
		response.setContentType("application/json");

		ForbiddenExceptionResponse exceptionResponse = new ForbiddenExceptionResponse("Acesso negado.");

		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(exceptionResponse);

		response.getWriter().write(json);
	}
}
