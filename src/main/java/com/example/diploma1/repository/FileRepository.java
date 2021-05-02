package com.example.diploma1.repository;

import com.example.diploma1.model.IncomingFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<IncomingFile, Long> {
    IncomingFile save(IncomingFile file);

    Optional<IncomingFile> findByFilename(String filename);

    @Transactional //don't know how exactly it works, but people advise adding this annotation to method or class
    //otherwise, an error appears: "No EntityManager with actual transaction available for current thread - cannot reliably process 'persist' call"
    void deleteByFilename(String filename);
}
