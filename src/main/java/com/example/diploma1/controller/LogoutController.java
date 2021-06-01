package com.example.diploma1.controller;

import com.example.diploma1.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@AllArgsConstructor
@Slf4j
public class LogoutController {

    private UserService userService;

    @GetMapping(value = "/login")
    public ResponseEntity<?> logout(@RequestHeader("User-Agent") String useragent, HttpServletRequest request) {
        var ip = request.getRemoteAddr();
        var hostname = request.getRemoteHost();
        log.debug("Exit attempt. ip:" + ip + " hostname:" + hostname + " User-Agent:" + useragent);

        String usernameFromToken;
        try {
            usernameFromToken = SecurityContextHolder.getContext().getAuthentication().getName();
        } catch (NullPointerException npe) {
            log.debug("Failure exit attempt. Wrong token. ip:" + ip + " hostname:" + hostname + " User-Agent:" + useragent);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("unreadable token");
        }

        var userDetails = userService.getUserByLogin(usernameFromToken);
        if (userDetails != null) {
            userService.deleteTokenByUsername(usernameFromToken);
            log.debug("Successful logout. User " + usernameFromToken + " has logged out");
            return ResponseEntity.status(HttpStatus.OK).body("user quit");
        }

        log.debug("Failed exit attempt. ip:" + ip + " hostname:" + hostname + " User-Agent:" + useragent);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("cant find such token");

    }
}
