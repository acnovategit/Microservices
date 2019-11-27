package com.webapp.service;

import com.webapp.model.User;

public interface UserService {

	
	public User findByMail(String mail);
	public void save(User user);
}
