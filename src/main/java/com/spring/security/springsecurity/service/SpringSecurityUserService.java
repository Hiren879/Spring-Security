package com.spring.security.springsecurity.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.spring.security.springsecurity.entity.User;
import com.spring.security.springsecurity.repository.UserRepository;

/**
 * This class will call any DB service/text file/excel to get the user
 * information. And return the UserDetails object. In our case we are using JPA
 * to fetch the user details from MySql DB and converting it into the
 * UserDetails.
 * 
 * @author Admin
 *
 */

@Service
public class SpringSecurityUserService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> user = userRepository.findByUserName(username);
		user.orElseThrow(() -> new UsernameNotFoundException("Not found for userName :: " + username));
		return user.map(MyUserDetails::new).get();
	}
}
