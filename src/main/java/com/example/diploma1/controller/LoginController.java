package com.example.diploma1.controller;

import com.example.diploma1.model.User;
import com.example.diploma1.security.JwtTokenUtil;
import com.example.diploma1.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@RestController
public class LoginController {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserService userService;

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    @PostMapping(value = "/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody User user, HttpServletRequest request) {
        var ip = request.getRemoteAddr();
        var hostname = request.getRemoteHost();
        var useragent = request.getHeader("User-Agent");

        log.info("Login attempt. ip:" + ip + " hostname:" + hostname + " User-Agent:" + useragent);
        final UserDetails userDetails = userService.getUserByLogin(user.getLogin());
        if (userDetails != null) {
            final String token = jwtTokenUtil.generateToken(userDetails);
            userService.addTokenToUser(user.getLogin(), token);
            log.info("Successful login. Access granted for user: " + user.getLogin() + ". token: " + token);
            HashMap<String, String> map = new HashMap<>();
            map.put("auth-token", token);
            return ResponseEntity.status(200).body(map);
        } else {
            log.info("Failure login attempt. Access denied for: ip:" + ip + " hostname:" + hostname + " User-Agent:" + useragent);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("such user not found");
        }
    }
}
