package com.example.diploma1.service;

import com.example.diploma1.model.User;
import com.example.diploma1.security.JwtTokenUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LoginFormDataValidator {
    private UserService userService;
    private JwtTokenUtil jwtTokenUtil;

    public String generateTokenIfUserIsRegistered(User user){
        final var userDetails = userService.getUserByLogin(user.getLogin());

        if (userDetails != null && checkUsernameAndPassword (user, userDetails)) {
            final var token = jwtTokenUtil.generateToken(userDetails);
            userService.addTokenToUser(user.getLogin(), token);
            return token;
        }
        return "";
    }

    private boolean checkUsernameAndPassword (User user, UserDetails userDetails){
        var name = userDetails.getUsername();
        var pass = userDetails.getPassword();

        return name.equals(user.getLogin()) && pass.equals(user.getPassword());
    }


}
