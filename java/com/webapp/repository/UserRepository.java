package com.webapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.webapp.model.User;

@Repository("userRepository")
public interface UserRepository extends JpaRepository<User,Long> {
	
	
	User findBymail(String mail);
	

}
