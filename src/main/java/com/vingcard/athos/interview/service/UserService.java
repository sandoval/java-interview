package com.vingcard.athos.interview.service;

import com.vingcard.athos.interview.dto.response.LoginTokenResponseDto;
import com.vingcard.athos.interview.persistence.entity.User;
import com.vingcard.athos.interview.dto.request.UserRegistrationRequestDto;

public interface UserService {

	User registerUser(UserRegistrationRequestDto userRegistrationRequestDto);

	LoginTokenResponseDto loginUser(String email, String password);

}
