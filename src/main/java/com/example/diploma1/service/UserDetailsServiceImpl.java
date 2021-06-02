package com.example.diploma1.service;

import com.example.diploma1.model.User;
import com.example.diploma1.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service("userDetailsService")
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        try {
            User user = usersRepository.findByLoginReturnUser(login);
            return usersRepository.findByLogin(login);
        } catch (NoSuchElementException e) {
            throw new UsernameNotFoundException("User " + login + " not found.", e);
        }
    }

}