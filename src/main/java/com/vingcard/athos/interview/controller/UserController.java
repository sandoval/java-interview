package com.vingcard.athos.interview.controller;

import com.vingcard.athos.interview.persistence.entity.auth.User;
import com.vingcard.athos.interview.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

	private UserService userService;


	/**
	 * Find all users from database
	 * Path: /api/users
	 * Method: GET
	 *
	 * @return List of all users from database
	 */
	@GetMapping
	public List<User> getUsers() {
		return userService.getUsers();
	}


	/**
	 * Filter user by email from database
	 * Path: /api/users/by-email/{email}
	 * Method: GET
	 *
	 * @param email User email
	 * @return User filtered by email Object
	 */
	@GetMapping("/by-email/{email}")
	public User getUserByEmail(@PathVariable String email) {
		return this.userService.getUserByEmail(email);
	}

}
