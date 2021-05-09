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
        User u1 = User.builder().login("maximgromak@gmail.com").password("12345").username("maximgromak@gmail.com").build();
        User u2 = User.builder().login("mr.dezolator@list.ru").password("54321").username("mr.dezolator@list.ru").build();
        User u3 = User.builder().login("a@a.ru").password("111").username("a@a.ru").build();

        userRepository.save(u1);
        userRepository.save(u2);
        userRepository.save(u3);
    }

}
