package com.example.diploma1.controller;

import com.example.diploma1.model.User;
import com.example.diploma1.security.JwtTokenUtil;
import com.example.diploma1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
public class LoginController {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserService userService;

    @PostMapping(value = "/login")
    public HashMap<String, String> createAuthenticationToken(@RequestBody User user) {
        final UserDetails userDetails = userService.getUserByLogin(user.getLogin());
        final String token = jwtTokenUtil.generateToken(userDetails);
        userService.addTokenToUser(user.getLogin(), token);

        HashMap<String, String> map = new HashMap<>();
        map.put("auth-token", token);
        return map;
    }
}
