package com.example.diploma1.controller;

import com.example.diploma1.model.IncomingFile;
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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@CrossOrigin(allowCredentials = "true", origins = "http://localhost:8080", maxAge = 3600)
@RestController
public class FileController {

    @Autowired
    FileService fileService;


    @GetMapping(value = "/list", headers = "Accept=application/json")
    @CrossOrigin
    public List<IncomingFile> showSavedFiles() {
        return fileService.show();
    }

    @PostMapping(value = "/file")
    @CrossOrigin
    public void saveFile(@RequestParam("filename") String filename, MultipartFile file) throws IOException {
        fileService.upload(file);
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

//    public static String convertStringToBinary(String input) {
//
//        StringBuilder result = new StringBuilder();
//        char[] chars = input.toCharArray();
//        for (char aChar : chars) {
//            result.append(
//                    String.format("%8s", Integer.toBinaryString(aChar))   // char -> int, auto-cast
//                            .replaceAll(" ", "0")                         // zero pads
//            );
//        }
//        return result.toString();
//
//    }
}
