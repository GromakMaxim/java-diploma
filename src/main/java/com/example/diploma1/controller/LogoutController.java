package com.example.diploma1.controller;

import com.example.diploma1.security.JwtTokenUtil;
import com.example.diploma1.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class LogoutController {

    private UserService userService;
    private JwtTokenUtil jwtTokenUtil;

    public LogoutController(UserService userService, JwtTokenUtil jwtTokenUtil) {
        this.userService = userService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    private static final Logger log = LoggerFactory.getLogger(LogoutController.class);

    @GetMapping(value = "/login")
    public ResponseEntity<?> logout(@RequestHeader("User-Agent") String useragent, HttpServletRequest request) {
        var ip = request.getRemoteAddr();
        var hostname = request.getRemoteHost();
        log.info("Exit attempt. ip:" + ip + " hostname:" + hostname + " User-Agent:" + useragent);

        String tokenRaw = request.getHeader("auth-token");
        String usernameFromToken;
        try {
            usernameFromToken = jwtTokenUtil.getUserNameFromTokenRaw(tokenRaw);
        } catch (NullPointerException npe) {
            log.info("Failure exit attempt. Wrong token. ip:" + ip + " hostname:" + hostname + " User-Agent:" + useragent);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("unreadable token");
        }

        var userDetails = userService.getUserByLogin(usernameFromToken);
        if (userDetails != null) {
            userService.deleteTokenByUsername(usernameFromToken);
            log.info("Successful logout. User " + usernameFromToken + " has logged out");
            return ResponseEntity.status(HttpStatus.OK).body("user quit");
        }

        log.info("Failed exit attempt. ip:" + ip + " hostname:" + hostname + " User-Agent:" + useragent);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("cant find such token");

    }
}
