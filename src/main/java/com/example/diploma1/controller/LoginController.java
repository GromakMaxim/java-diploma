package com.example.diploma1.controller;

import com.example.diploma1.model.User;
import com.example.diploma1.service.LoginFormDataValidator;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@RestController
@AllArgsConstructor
@Slf4j
public class LoginController {

    private LoginFormDataValidator loginFormDataValidator;

    @PostMapping(value = "/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestHeader("User-Agent") String useragent, @RequestBody User user, HttpServletRequest request) {
        var ip = request.getRemoteAddr();
        var hostname = request.getRemoteHost();
        log.debug("Login attempt. ip:" + ip + " hostname:" + hostname + " User-Agent:" + useragent);

        //check request data with db
        var token = loginFormDataValidator.generateTokenIfUserIsRegistered(user);

        if (!token.equals("")){
            log.debug("Successful login. Access granted for user: " + user.getLogin() + ". token: " + token);
            HashMap<String, String> map = new HashMap<>();
            map.put("auth-token", token);
            return ResponseEntity.status(200).body(map);
        }
        log.debug("Failure login attempt. Access denied for: ip:" + ip + " hostname:" + hostname + " User-Agent:" + useragent);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("such user not found");

    }

}
