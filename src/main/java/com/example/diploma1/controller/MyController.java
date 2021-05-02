package com.example.diploma1.controller;

import com.example.diploma1.model.IncomingFile;
import com.example.diploma1.model.User;
import com.example.diploma1.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@CrossOrigin(allowCredentials = "true", origins = "http://localhost:8080", maxAge = 3600)
@RestController
public class MyController{

    @Autowired
    FileService fileService;


    @PostMapping("/login")
    public HashMap<String, String> login(@RequestBody User user) {
        HashMap<String, String> map = new HashMap<>();
        map.put("auth-token", "qwerty");
        return map;
    }

    @GetMapping(value = "/list", headers = "Accept=application/json")
    @CrossOrigin
    public List<IncomingFile> showSavedFiles() {
        return fileService.show();
    }

    @PostMapping(value = "/file")
    @CrossOrigin
    public void saveFile(@RequestParam("filename") String filename, MultipartFile file) {
        fileService.upload(file);
    }

    @GetMapping(value = "/file")
    @CrossOrigin
    public Object downloadFile(@RequestParam("filename") String filename){
        return fileService.download(filename);
    }

    @DeleteMapping(value = "/file")
    @CrossOrigin
    public void delete(@RequestParam("filename")String filename){
        fileService.delete(filename);
    }
}
