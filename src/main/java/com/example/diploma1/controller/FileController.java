package com.example.diploma1.controller;

import com.example.diploma1.model.IncomingFile;
import com.example.diploma1.security.JwtTokenUtil;
import com.example.diploma1.service.FileService;
import com.example.diploma1.service.UserService;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;

@RestController
public class FileController {

    @Autowired
    FileService fileService;

    @Autowired
    UserService userService;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    private static final Logger log = LoggerFactory.getLogger(FileController.class);

    @GetMapping(value = "/list")
    public Object showSavedFiles(HttpServletRequest request) {
        String tokenRaw = request.getHeader("auth-token");
        var usernameFromToken = jwtTokenUtil.getUserNameFromTokenRaw(tokenRaw);
        if (usernameFromToken != null) {
            return fileService.show(usernameFromToken);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("unauthorized attempt to access files");
        }
    }

    @PostMapping(value = "/file")
    public ResponseEntity<?> saveFile(@RequestParam("filename") String filename, MultipartFile file, HttpServletRequest request) throws IOException {
        var ip = request.getRemoteAddr();
        var hostname = request.getRemoteHost();
        var useragent = request.getHeader("User-Agent");
        log.info("Upload attempt. ip" + ip + " hostname:" + hostname + " User-Agent:" + useragent);

        String tokenRaw = request.getHeader("auth-token");
        var usernameFromToken = jwtTokenUtil.getUserNameFromTokenRaw(tokenRaw);
        final UserDetails userDetails = userService.getUserByLogin(usernameFromToken);

        if (userDetails == null) {
            log.info("Failed upload attempt. ip:" + ip + " hostname:" + hostname + " User-Agent:" + useragent);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("cant find such token");
        }

        fileService.upload(file, request);
        log.info("Success upload attempt. User " + usernameFromToken + " uploaded file " + file.getOriginalFilename());
        return ResponseEntity.status(200).build();
    }

    @GetMapping(value = "/file")
    public ResponseEntity<Object> downloadFile(@RequestParam("filename") String filename, HttpServletRequest request) {
        var ip = request.getRemoteAddr();
        var hostname = request.getRemoteHost();
        var useragent = request.getHeader("User-Agent");

        String tokenRaw = request.getHeader("auth-token");
        var usernameFromToken = jwtTokenUtil.getUserNameFromTokenRaw(tokenRaw);
        final UserDetails userDetails = userService.getUserByLogin(usernameFromToken);

        if (userDetails == null) {
            log.info("Failed download attempt. ip:" + ip + " hostname:" + hostname + " User-Agent:" + useragent);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("cant find such token");
        }

        IncomingFile file = fileService.download(filename);
        InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(file.getFileContent()));
        HttpHeaders headers = new HttpHeaders();

        headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getFilename()));
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        ResponseEntity<Object> responseEntity = ResponseEntity.ok().headers(headers).contentLength(
                file.getFileContent().length).contentType(MediaType.parseMediaType(file.getFileType())).body(resource);

        return responseEntity;
    }

    @DeleteMapping(value = "/file")
    public ResponseEntity<?> deleteFile(@RequestParam("filename") String filename, HttpServletRequest request) {
        var ip = request.getRemoteAddr();
        var hostname = request.getRemoteHost();
        var useragent = request.getHeader("User-Agent");

        String tokenRaw = request.getHeader("auth-token");
        var usernameFromToken = jwtTokenUtil.getUserNameFromTokenRaw(tokenRaw);
        final UserDetails userDetails = userService.getUserByLogin(usernameFromToken);

        if (userDetails == null) {
            log.info("Failed delete attempt. ip:" + ip + " hostname:" + hostname + " User-Agent:" + useragent);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("cant find such token");
        }

        fileService.delete(filename);
        log.info("User " + usernameFromToken + " successfully deleted file " + filename);
        return ResponseEntity.status(200).build();
    }

    @PutMapping(value = "/file")
    public ResponseEntity<?> renameFile(@RequestParam("filename") String filename, @RequestBody String json, HttpServletRequest request) throws JSONException {
        var ip = request.getRemoteAddr();
        var hostname = request.getRemoteHost();
        var useragent = request.getHeader("User-Agent");

        String tokenRaw = request.getHeader("auth-token");
        var usernameFromToken = jwtTokenUtil.getUserNameFromTokenRaw(tokenRaw);
        final UserDetails userDetails = userService.getUserByLogin(usernameFromToken);

        if (userDetails == null) {
            log.info("Failed rename attempt. ip:" + ip + " hostname:" + hostname + " User-Agent:" + useragent);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("cant find such token");
        }

        JSONObject jsonObject = new JSONObject(json);
        fileService.rename(filename, jsonObject.get("filename").toString());
        log.info("User " + usernameFromToken + " renamed file " + filename);
        return ResponseEntity.status(200).build();
    }

}