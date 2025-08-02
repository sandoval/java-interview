package com.vingcard.athos.interview.service;

import com.vingcard.athos.interview.persistence.entity.auth.User;

import java.util.List;

public interface UserService {

	List<User> getUsers();

	User getUserByEmail(String email);

}
