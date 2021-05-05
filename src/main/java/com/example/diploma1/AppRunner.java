package com.example.diploma1;

import com.example.diploma1.model.IncomingFile;
import com.example.diploma1.model.User;
import com.example.diploma1.repository.FileRepository;
import com.example.diploma1.repository.UserRepository;
import com.example.diploma1.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@Component
public class AppRunner implements CommandLineRunner {
    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public void run(String... args) {
        User u1 = User.builder().login("maximgromak@gmail.com").password("12345").build();
        User u2 = User.builder().login("mr.dezolator@list.ru").password("54321").build();
        User u3 = User.builder().login("aaa@a.ru").password("111").build();

        userRepository.save(u1);
        userRepository.save(u2);
        userRepository.save(u3);

        fileRepository.save(IncomingFile.builder()
                .filename("qwertyOne.txt")
                .key(UUID.randomUUID().toString())
                .size(10_000L)
                .uploadDate(LocalDate.now())
                .user(u1)
                .build());

        fileRepository.save(IncomingFile.builder()
                .filename("qwertyTwo.ppx")
                .key(UUID.randomUUID().toString())
                .size(10_000L)
                .uploadDate(LocalDate.now())
                .user(u2)
                .build());

        fileRepository.save(IncomingFile.builder()
                .filename("qwertyThree.exe")
                .key(UUID.randomUUID().toString())
                .size(10_000L)
                .uploadDate(LocalDate.now())
                .user(u3)
                .build());
    }

}
