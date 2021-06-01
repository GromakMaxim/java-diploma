package com.example.diploma1.service;

import com.example.diploma1.model.IncomingFile;
import com.example.diploma1.repository.FileRepository;
import com.example.diploma1.security.JwtTokenUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class FileService{

    private FileRepository fileRepository;
    private UserService userService;
    private JwtTokenUtil jwtTokenUtil;

    @Transactional(rollbackOn = IOException.class)//в случае падения IOException, все наши сохранения в БД откатятся?
    public void upload(MultipartFile resource, HttpServletRequest request) throws IOException {
        var tokenRaw = request.getHeader("auth-token");
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
        return fileRepository.findByFilename(filename).orElse(null);
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
