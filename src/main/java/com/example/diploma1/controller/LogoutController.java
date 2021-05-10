package com.example.diploma1.controller;

import com.example.diploma1.security.JwtRequestFilter;
import com.example.diploma1.security.JwtTokenUtil;
import com.example.diploma1.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
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

    private static final Logger log = LoggerFactory.getLogger(LogoutController.class);

    @GetMapping(value = "/login")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        var ip = request.getRemoteAddr();
        var hostname = request.getRemoteHost();
        var useragent = request.getHeader("User-Agent");
        log.info("Exit attempt. ip:" + ip + " hostname:" + hostname + " User-Agent:" + useragent);

        String tokenRaw = request.getHeader("auth-token");
        var usernameFromToken = jwtTokenUtil.getUserNameFromTokenRaw(tokenRaw);
        final UserDetails userDetails = userService.getUserByLogin(usernameFromToken);

        if (userDetails == null) {
            log.info("Failed exit attempt. ip:" + ip + " hostname:" + hostname + " User-Agent:" + useragent);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("cant find such token");
        }

        userService.deleteTokenByUsername(usernameFromToken);
        log.info("Successful logout. User " + usernameFromToken + " has logged out");
        return ResponseEntity.status(HttpStatus.OK).body("user quit");
    }
}
