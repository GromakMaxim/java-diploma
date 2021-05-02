package com.example.diploma1;

import com.example.diploma1.model.IncomingFile;
import com.example.diploma1.repository.FileRepository;
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

    @Override
    @Transactional
    public void run(String... args) {
        fileRepository.save(IncomingFile.builder().filename("qwertyOne.txt").key(UUID.randomUUID().toString()).size(10_000L).uploadDate(LocalDate.now()).build());
        fileRepository.save(IncomingFile.builder().filename("qwertyTwo.ppx").key(UUID.randomUUID().toString()).size(10_000L).uploadDate(LocalDate.now()).build());
        fileRepository.save(IncomingFile.builder().filename("qwertyThree.exe").key(UUID.randomUUID().toString()).size(10_000L).uploadDate(LocalDate.now()).build());
    }
}
