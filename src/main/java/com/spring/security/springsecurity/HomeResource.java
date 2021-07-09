package com.spring.security.springsecurity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeResource {

	@GetMapping("/")
	public String getHome() {
		return "<h1>Welcome Home !!</h1>";
	}
	
	
	@GetMapping("/user")
	public String getUser() {
		return "<h1>Welcome User !!</h1>";
	}
	
	@GetMapping("/admin")
	public String getAdmin() {
		return "<h1>Welcome Admin !!</h1>";
	}
}
