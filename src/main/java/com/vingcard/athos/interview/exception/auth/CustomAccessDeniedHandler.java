package com.vingcard.athos.interview.exception.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Locale;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

	private final MessageSource messageSource;

	@Autowired
	public CustomAccessDeniedHandler(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	@Override
	public void handle(HttpServletRequest request,
	                   HttpServletResponse response,
	                   AccessDeniedException accessDeniedException) throws IOException {

		response.setStatus(HttpStatus.FORBIDDEN.value());
		response.setContentType("application/json");

		ForbiddenExceptionResponse exceptionResponse =
				new ForbiddenExceptionResponse(messageSource.getMessage("error.access_denied",
						null,
						Locale.getDefault()));

		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(exceptionResponse);

		response.getWriter().write(json);
	}
}
