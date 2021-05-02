package com.example.diploma1.controller;

import com.example.diploma1.model.ThatsMyFile;
import com.example.diploma1.model.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@CrossOrigin(allowCredentials = "true", origins = "http://localhost:8080", maxAge = 3600)
@RestController
public class MyController implements HandlerInterceptor {


    @PostMapping("/login")
    public HashMap<String, String> login(@RequestBody User user) {
        HashMap<String, String> map = new HashMap<>();
        map.put("auth-token", "qwerty");
        return map;
    }

    @GetMapping(value = "/list", headers = "Accept=application/json")
    public List showSavedFiles() {
        List<ThatsMyFile> list = new ArrayList<>();
        list.add(new ThatsMyFile("first.doc",5));
        list.add(new ThatsMyFile("sec.txt",5));
        list.add(new ThatsMyFile("third.xlsx",5));
        return list;
    }

    @PostMapping(value = "/file")
    @CrossOrigin(allowedHeaders = "Access-Control-Allow-Origin")
    public void saveFile(@RequestParam("filename") String filename, MultipartFile file) throws IOException {
        File convertFile = new File(file.getOriginalFilename());
        convertFile.createNewFile();
        FileOutputStream fout = new FileOutputStream(convertFile);
        fout.write(file.getBytes());
        fout.close();
        System.out.println("File is upload successfully");
    }
}
