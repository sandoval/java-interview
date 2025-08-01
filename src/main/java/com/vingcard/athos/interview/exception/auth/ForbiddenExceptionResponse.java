package com.vingcard.athos.interview.exception.auth;

import com.amazonaws.services.cognitoidp.model.ForbiddenException;

public class ForbiddenExceptionResponse extends ForbiddenException {
	public ForbiddenExceptionResponse(String message) {
		super(message);
	}
}
