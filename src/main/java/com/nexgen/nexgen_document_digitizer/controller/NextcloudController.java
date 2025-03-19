package com.nexgen.nexgen_document_digitizer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nexgen.nexgen_document_digitizer.service.NextcloudService;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/nextcloud")
public class NextcloudController {

    @Autowired
    private NextcloudService nextcloudService;

    @PutMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String response = nextcloudService.uploadFile(file); // Use response from service
            return response;  // Return actual success or failure message
        } catch (Exception e) {
            e.printStackTrace();
            return "File upload failed: " + e.getMessage();
        }
    }

    // 🔹 Endpoint to View File
    @GetMapping("/view")
    public ResponseEntity<byte[]> viewFile(@RequestParam String fileName) {
        return nextcloudService.viewFile(fileName);
    }

    // @GetMapping("/download/{fileName}")
    // public File downloadFile(@PathVariable String fileName) {
    //     try {
    //         return nextcloudService.downloadFile(fileName);
    //     } catch (IOException e) {
    //         e.printStackTrace();
    //         return null;
    //     }
    // }
}
