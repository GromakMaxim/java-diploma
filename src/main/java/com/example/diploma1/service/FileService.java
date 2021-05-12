package com.example.diploma1.service;

import com.example.diploma1.model.IncomingFile;
import com.example.diploma1.repository.FileRepository;
import com.example.diploma1.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
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

    @Autowired
    private UserService userService;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Transactional(rollbackOn = IOException.class)//в случае падения IOException, все наши сохранения в БД откатятся?
    public void upload(MultipartFile resource, HttpServletRequest request) throws IOException {
        String tokenRaw = request.getHeader("auth-token");
        var usernameFromToken = jwtTokenUtil.getUserNameFromTokenRaw(tokenRaw);
        var user = userService.getUserByLoginReturnUser(usernameFromToken);

        IncomingFile file = IncomingFile.builder()
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
        Optional<IncomingFile> file = fileRepository.findByFilename(filename);
        return file.isPresent() ? file.get() : null;
    }

    public void delete(String filename, String username) {
        fileRepository.deleteByFilename(filename, username);
    }


    public List<IncomingFile> show(String username) {
        return fileRepository.findAllFilesByUsername(username);
    }

    public void rename(String originalFilename, String targetFileName) {
        fileRepository.rename(originalFilename,targetFileName);
    }
}
