package com.example.diploma1.controller;

import com.example.diploma1.model.IncomingFile;
import com.example.diploma1.service.FileService;
import com.example.diploma1.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;

@RestController
@AllArgsConstructor
@Slf4j
public class FileController {

    private FileService fileService;
    private UserService userService;

    @GetMapping(value = "/list")
    public Object showSavedFiles(@RequestHeader("User-Agent") String useragent, HttpServletRequest request) {
        var ip = request.getRemoteAddr();
        var hostname = request.getRemoteHost();
        log.debug("Viewing files attempt. ip:" + ip + " hostname:" + hostname + " User-Agent:" + useragent);

        var usernameFromToken = SecurityContextHolder.getContext().getAuthentication().getName();
        if (usernameFromToken.equalsIgnoreCase("anonymousUser")){
            log.debug("Failure viewing attempt. Wrong token. ip:" + ip + " hostname:" + hostname + " User-Agent:" + useragent);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("wrong token");
        }

        var userDetails = userService.getUserByLogin(usernameFromToken);
        if (userDetails != null) {
            log.debug("Successful viewing attempt. Access granted for user: " + usernameFromToken);
            return fileService.show(usernameFromToken);
        }
        log.debug("Failure viewing attempt. Wrong token. ip:" + ip + " hostname:" + hostname + " User-Agent:" + useragent);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("unauthorized attempt to access files");

    }

    @PostMapping(value = "/file")
    public ResponseEntity<?> saveFile(@RequestHeader("User-Agent") String useragent, MultipartFile file, HttpServletRequest request) throws IOException {
        var ip = request.getRemoteAddr();
        var hostname = request.getRemoteHost();
        log.debug("Upload attempt. ip" + ip + " hostname:" + hostname + " User-Agent:" + useragent);

        var usernameFromToken = SecurityContextHolder.getContext().getAuthentication().getName();
        if (usernameFromToken.equalsIgnoreCase("anonymousUser")){
            log.debug("Failed upload attempt. ip:" + ip + " hostname:" + hostname + " User-Agent:" + useragent);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("cant find such token");
        }

        var userDetails = userService.getUserByLogin(usernameFromToken);
        if (userDetails != null) {
            fileService.upload(file);
            log.debug("Success upload attempt. User " + usernameFromToken + " uploaded file " + file.getOriginalFilename());
            return ResponseEntity.status(200).build();
        }
        log.debug("Failed upload attempt. ip:" + ip + " hostname:" + hostname + " User-Agent:" + useragent);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("cant find such token");
    }

    @GetMapping(value = "/file")
    public ResponseEntity<Object> downloadFile(@RequestHeader("User-Agent") String useragent, @RequestParam("filename") String filename, HttpServletRequest request) {
        var ip = request.getRemoteAddr();
        var hostname = request.getRemoteHost();
        log.debug("Download attempt. ip" + ip + " hostname:" + hostname + " User-Agent:" + useragent);

        var usernameFromToken = SecurityContextHolder.getContext().getAuthentication().getName();
        if (usernameFromToken.equalsIgnoreCase("anonymousUser")){
            log.debug("Failure downloading attempt. Wrong token. ip:" + ip + " hostname:" + hostname + " User-Agent:" + useragent);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("unreadable token");
        }

        var userDetails = userService.getUserByLogin(usernameFromToken);
        if (userDetails != null) {
            IncomingFile file = fileService.download(filename);
            InputStreamResource resource = new InputStreamResource(new ByteArrayInputStream(file.getFileContent()));
            HttpHeaders headers = new HttpHeaders();

            headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getFilename()));
            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");

            return ResponseEntity.ok().headers(headers).contentLength(
                    file.getFileContent().length).contentType(MediaType.parseMediaType(file.getFileType())).body(resource);
        }

        log.debug("Failed download attempt. ip:" + ip + " hostname:" + hostname + " User-Agent:" + useragent);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("cant find such token");
    }

    @DeleteMapping(value = "/file")
    public ResponseEntity<?> deleteFile(@RequestHeader("User-Agent") String useragent, @RequestParam("filename") String filename, HttpServletRequest request) {
        var ip = request.getRemoteAddr();
        var hostname = request.getRemoteHost();
        log.debug("Delete attempt. ip" + ip + " hostname:" + hostname + " User-Agent:" + useragent);

        var usernameFromToken = SecurityContextHolder.getContext().getAuthentication().getName();
        if (usernameFromToken.equalsIgnoreCase("anonymousUser")){
            log.debug("Failed delete attempt. ip:" + ip + " hostname:" + hostname + " User-Agent:" + useragent);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("cant find such token");
        }

        var userDetails = userService.getUserByLogin(usernameFromToken);
        if (userDetails != null) {
            fileService.delete(filename, userDetails.getUsername());
            log.debug("User " + usernameFromToken + " successfully deleted file " + filename);
            return ResponseEntity.ok("successfully deleted");
        }
        log.debug("Failed delete attempt. Wrong token. ip:" + ip + " hostname:" + hostname + " User-Agent:" + useragent);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("unauthorized attempt delete files");
    }

    @PutMapping(value = "/file")
    public ResponseEntity<?> renameFile(@RequestHeader("User-Agent") String useragent, @RequestParam("filename") String filename, @RequestBody String json, HttpServletRequest request) throws JSONException {
        var ip = request.getRemoteAddr();
        var hostname = request.getRemoteHost();
        log.debug("Renaming attempt. ip" + ip + " hostname:" + hostname + " User-Agent:" + useragent);

        var usernameFromToken = SecurityContextHolder.getContext().getAuthentication().getName();
        if (usernameFromToken.equalsIgnoreCase("anonymousUser")){
            log.debug("Failure renaming attempt. Wrong token. ip:" + ip + " hostname:" + hostname + " User-Agent:" + useragent);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("unreadable token");
        }

        var userDetails = userService.getUserByLogin(usernameFromToken);
        if (userDetails != null) {
            JSONObject jsonObject = new JSONObject(json);
            fileService.rename(filename, jsonObject.get("filename").toString());
            log.debug("User " + usernameFromToken + " renamed file " + filename);
            return ResponseEntity.status(200).body("successfully renamed");
        }

        log.debug("Failed rename attempt. ip:" + ip + " hostname:" + hostname + " User-Agent:" + useragent);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("cant find such token");
    }

}