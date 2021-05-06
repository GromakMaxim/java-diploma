package com.example.diploma1;

import com.example.diploma1.model.User;
import com.example.diploma1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AppRunner implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public void run(String... args) {
        User u1 = User.builder().username("maximgromak@gmail.com").password("12345").build();
        User u2 = User.builder().username("mr.dezolator@list.ru").password("54321").build();
        User u3 = User.builder().username("a@a.ru").password("111").build();

        userRepository.save(u1);
        userRepository.save(u2);
        userRepository.save(u3);
    }

}
