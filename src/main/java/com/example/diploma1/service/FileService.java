package com.example.diploma1.service;

import com.example.diploma1.model.IncomingFile;
import com.example.diploma1.repository.FileRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class FileService {

    private FileRepository fileRepository;
    private UserService userService;

    public void upload(MultipartFile resource) throws IOException {
        var usernameFromToken = SecurityContextHolder.getContext().getAuthentication().getName();
        var user = userService.getUserByLoginReturnUser(usernameFromToken);

        var file = IncomingFile.builder()
                .filename(resource.getOriginalFilename())
                .key(UUID.randomUUID().toString())
                .size(resource.getSize())
                .uploadDate(LocalDate.now())
                .fileType(resource.getContentType())
                .fileContent(resource.getInputStream().readAllBytes())
                .user(user)
                .build();
        fileRepository.save(file);
    }

    public IncomingFile download(String filename) {
        return fileRepository.findByFilename(filename).orElse(null);
    }

    public void delete(String filename, String username) {
        fileRepository.deleteByFilename(filename, username);
    }


    public List<IncomingFile> show(String username) {
        return fileRepository.findAllFilesByUsername(username);
    }

    public void rename(String originalFilename, String targetFileName) {
        fileRepository.rename(originalFilename, targetFileName);
    }
}
