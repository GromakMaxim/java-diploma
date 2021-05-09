package com.example.diploma1.service;

import com.example.diploma1.model.User;
import com.example.diploma1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Transactional(rollbackOn = IOException.class)//в случае падения IOException, все наши сохранения в БД откатятся?
    public void createUser(User userData){
        User user = User.builder()
                .login(userData.getLogin())
                .password(userData.getPassword())
                .build();
        userRepository.save(user);
    }

    public UserDetails getUserByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    public void addTokenToUser(String login, String token) {
        userRepository.addTokenToUser(login, token);
    }

    public void deleteTokenByUsername(String username) {
        userRepository.deleteTokenByUsername(username);
    }
}
