package com.spring.security.springsecurity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeResource {

	@GetMapping("/")
	public String getHome() {
		return "<h1>Welcome Home !!</h1>";
	}
	
}
