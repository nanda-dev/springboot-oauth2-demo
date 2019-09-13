package com.ng.oauth2.demo.resource;

import javax.annotation.security.RolesAllowed;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class RestResource 
{
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
}
