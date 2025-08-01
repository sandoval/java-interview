package com.vingcard.athos.interview.service;

import com.vingcard.athos.interview.dto.LoginTokenResponseDto;
import com.vingcard.athos.interview.persistence.entity.User;
import com.vingcard.athos.interview.requests.UserRegistrationRequest;

public interface UserService {

	User registerUser(UserRegistrationRequest userRegistrationRequest);

	LoginTokenResponseDto loginUser(String email, String password);
}
