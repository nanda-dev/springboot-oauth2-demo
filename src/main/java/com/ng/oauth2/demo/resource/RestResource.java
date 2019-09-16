package com.ng.oauth2.demo.resource;

import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class RestResource 
{
	@Autowired private PasswordEncoder passwordEncoder;
	
	@RolesAllowed({"USER", "ADMIN"})
	@GetMapping("/api/users/me")
	public String profile() 
	{
		//User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		log.info("logged in user: {}", SecurityContextHolder.getContext().getAuthentication().getName());

		return "testuser@demo.com";
	}
	
	@RolesAllowed({"USER"})
	@GetMapping("/api/user")
	public String user() {
		return "USER";
	}
	
	@RolesAllowed({"ADMIN"})
	@GetMapping("/admin")
	public String admin() {
		return "ADMIN";
	}
	
	@GetMapping("/encode/{str}")
	public String encode(@PathVariable String str) {
		return passwordEncoder.encode(str);
	}	
	
}
