package com.example.diploma1.service;

import com.example.diploma1.model.IncomingFile;
import com.example.diploma1.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class FileService{

    @Autowired
    private FileRepository fileRepository;

    @Transactional(rollbackOn = IOException.class)//в случае падения IOException, все наши сохранения в БД откатятся?
    public void upload(MultipartFile resource) throws IOException {
        IncomingFile file = IncomingFile.builder()
                .filename(resource.getOriginalFilename())
                .key(UUID.randomUUID().toString())
                .size(resource.getSize())
                .uploadDate(LocalDate.now())
                .fileType(resource.getContentType())
                .fileContent(resource.getInputStream().readAllBytes())
                .build();
        fileRepository.save(file);
    }

    public IncomingFile download(String filename) {
        Optional<IncomingFile> file = fileRepository.findByFilename(filename);
        return file.isPresent() ? file.get() : null;
    }

    public void delete(String filename) {
        fileRepository.deleteByFilename(filename);
    }

    public List<IncomingFile> show() {
        return fileRepository.findAll();
    }

    public void rename(String originalFilename, String targetFileName) {
        fileRepository.rename(originalFilename,targetFileName);
    }
}
