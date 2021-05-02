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
    public List showSavedFiles() {
        List<IncomingFile> list = new ArrayList<>();
        list.add(new IncomingFile("first.doc", 5));
        list.add(new IncomingFile("sec.txt", 5));
        list.add(new IncomingFile("third.xlsx", 5));
        return list;
    }

    @PostMapping(value = "/file")
    @CrossOrigin
    public void saveFile(@RequestParam("filename") String filename, MultipartFile file) throws IOException {
        fileService.upload(file);
    }
}
