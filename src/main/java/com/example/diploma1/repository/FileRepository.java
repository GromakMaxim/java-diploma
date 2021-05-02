package com.example.diploma1.repository;

import com.example.diploma1.model.IncomingFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<IncomingFile, Long> {
    IncomingFile save(IncomingFile file);
}
