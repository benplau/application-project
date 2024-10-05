package com.application.project.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.application.project.model.LoginDTO;
import com.application.project.model.User;
import com.application.project.model.UserDTO;
import com.application.project.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository m_userRepository;

	public Map<String, Object> addUser(UserDTO userDTO) {
		if (m_userRepository.findByUsername(userDTO.getUsername()) != null) {
			return Map.of("status", "USER_ALREADY_EXIST");
		} else {

			User user = new User(
					userDTO.getUserId(), 
					userDTO.getUsername(), 
					userDTO.getEmail(), 
					userDTO.getPassword());

			m_userRepository.save(user);

			return Map.of("status", "CREATE_ACCOUNT_SUCCESS");
		}
	}

	public Map<String, Object> loginUser(LoginDTO loginDTO) {
		User user = m_userRepository.findByUsername(loginDTO.getUsername());

		if (user == null) {
			return Map.of("status", "USER_NOT_FOUND");
		} else {
			String password = loginDTO.getPassword();

			String userPassword = user.getPassword();

			if (password.equals(userPassword)) {
				return Map.of(
						"status", "LOGIN_SUCCESS",
						"id",user.getId(),
						"username",user.getUsername());
			} else {
				return Map.of("status", "INCORRECT_PASSWORD");
			}
		}
	}

}
