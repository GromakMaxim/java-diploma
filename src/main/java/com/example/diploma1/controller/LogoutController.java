package com.example.diploma1.controller;

import com.example.diploma1.security.JwtRequestFilter;
import com.example.diploma1.security.JwtTokenUtil;
import com.example.diploma1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class LogoutController {

    @Autowired
    UserService userService;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    JwtRequestFilter jwtRequestFilter;

    @GetMapping(value = "/login")
    public void logout(HttpServletRequest request) {
        String tokenRaw = request.getHeader("auth-token");
        var usernameFromToken = jwtTokenUtil.getUserNameFromTokenRaw(tokenRaw);
        userService.deleteTokenByUsername(usernameFromToken);
    }
}
