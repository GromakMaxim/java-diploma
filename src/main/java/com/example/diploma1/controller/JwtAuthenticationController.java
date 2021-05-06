package com.example.diploma1.controller;

import com.example.diploma1.model.JwtRequest;
import com.example.diploma1.security.JwtTokenUtil;
import com.example.diploma1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
public class JwtAuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserService userService;


    @PostMapping(value = "/login")
    public HashMap<String, String> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
        System.out.println(authenticationRequest.getLogin());
        System.out.println(authenticationRequest.getPassword());
        //authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());


        final UserDetails userDetails = userService.getUserByLogin(authenticationRequest.getLogin());

        final String token = jwtTokenUtil.generateToken(userDetails);
        HashMap<String, String> map = new HashMap<>();
        map.put("auth-token", token);
        return map;
    }

    private void authenticate(String username, String password) throws Exception {
        var t = new UsernamePasswordAuthenticationToken(username, password);
        try {
            authenticationManager.authenticate(t);
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}
