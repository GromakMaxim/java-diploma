package com.example.diploma1.repository;

import com.example.diploma1.model.IncomingFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface FileRepository extends JpaRepository<IncomingFile, Long> {
    IncomingFile save(IncomingFile file);

    Optional<IncomingFile> findByFilename(String filename);

    void deleteByFilename(String filename);

    @Modifying
    @Query("update IncomingFile file set file.filename = :targetFileName where file.filename=:originalFilename")
    void rename(@Param("originalFilename") String originalFilename, @Param("targetFileName") String targetFileName);
}
