package com.example.diploma1.controller;

import com.example.diploma1.model.IncomingFile;
import com.example.diploma1.security.JwtTokenUtil;
import com.example.diploma1.service.FileService;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@RestController
public class FileController {

    @Autowired
    FileService fileService;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @GetMapping(value = "/list")
    public List<IncomingFile> showSavedFiles(HttpServletRequest request) {
        String tokenRaw = request.getHeader("auth-token");
        var usernameFromToken = jwtTokenUtil.getUserNameFromTokenRaw(tokenRaw);
        return fileService.show(usernameFromToken);
    }


    @PostMapping(value = "/file")
    public void saveFile(@RequestParam("filename") String filename, MultipartFile file, HttpServletRequest request) throws IOException {
        fileService.upload(file, request);
    }

    @GetMapping(value = "/file")
    @CrossOrigin
    public ResponseEntity<Object> downloadFile(@RequestParam("filename") String filename) throws IOException {
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
    @CrossOrigin
    public void deleteFile(@RequestParam("filename") String filename) {
        fileService.delete(filename);
    }

    @PutMapping(value = "/file")
    @CrossOrigin
    public void renameFile(@RequestParam("filename") String filename, @RequestBody String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);
        fileService.rename(filename, jsonObject.get("filename").toString());
    }
}
