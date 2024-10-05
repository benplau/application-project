package com.application.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.application.project.model.LoginDTO;
import com.application.project.model.UserDTO;
import com.application.project.service.UserService;

@RestController
@CrossOrigin
@RequestMapping(path = "/user-auth")
public class UserAuthController {

	@Autowired
	private UserService m_userService;

	@PostMapping(path = "/create-account")
	public Object saveEmployee(@RequestBody UserDTO userDTO) {
		return m_userService.addUser(userDTO);
	}

	@PostMapping(path = "/login")
	public Object loginEmployee(@RequestBody LoginDTO loginDTO) {
		return m_userService.loginUser(loginDTO);
	}

}
