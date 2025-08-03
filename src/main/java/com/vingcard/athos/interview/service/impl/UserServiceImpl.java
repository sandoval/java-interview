package com.vingcard.athos.interview.service.impl;

import com.vingcard.athos.interview.exception.ResourceNotFoundException;
import com.vingcard.athos.interview.persistence.entity.auth.User;
import com.vingcard.athos.interview.persistence.repository.UserRepository;
import com.vingcard.athos.interview.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;


	/**
	 * Get all user from database
	 *
	 * @return List of database users
	 */
	@Override
	public List<User> getUsers() {
		return this.userRepository.findAll();
	}


	/**
	 * Filter users by Email
	 *
	 * @param email User email
	 * @return User filtered by email
	 */
	@Override
	public User getUserByEmail(String email) {
		return this.userRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("User not found: " + email));
	}
}
