package com.example.diploma1.service;

import com.example.diploma1.model.User;
import com.example.diploma1.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;

    public UserDetails getUserByLogin(String login) {
        return userRepository.findByLogin(login);
    }
    public User getUserByLoginReturnUser(String login) {
        return userRepository.findByLoginReturnUser(login);
    }

    public void addTokenToUser(String login, String token) {
        userRepository.addTokenToUser(login, token);
    }

    public void deleteTokenByUsername(String username) {
        userRepository.deleteTokenByUsername(username);
    }
}
