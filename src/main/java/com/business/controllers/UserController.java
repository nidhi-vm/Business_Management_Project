package com.business.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.business.entities.User;
import com.business.services.UserServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class UserController
{
	private static final String REDIRECT_SERVICES = "redirect:/admin/services";
	
	@Autowired
	private UserServices services;

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@PostMapping("/addingUser")
	public String  addUser(@ModelAttribute User user)
	{
		logger.info("User added: {}", user);
		this.services.addUser(user);
		return REDIRECT_SERVICES;
	}

	@GetMapping("/updatingUser/{id}")
	public String updateUser(@ModelAttribute User user, @PathVariable("id") int id)
	{
		this.services.updateUser(user, id);
		return REDIRECT_SERVICES;
	}

	@GetMapping("/deleteUser/{id}")
	public String deleteUser(@PathVariable("id" )int id)
	{
		this.services.deleteUser(id);
		return REDIRECT_SERVICES;
	}
}