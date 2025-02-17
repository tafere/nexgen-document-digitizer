// package com.nexgen.nexgen_document_digitizer.controller;

// import java.io.File;
// import java.io.IOException;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.RestController;
// import org.springframework.web.multipart.MultipartFile;

// import com.nexgen.nexgen_document_digitizer.service.NextcloudService;

// @RestController
// @RequestMapping("/nextcloud")
// public class NextcloudController {

//     @Autowired
//     private NextcloudService nextcloudService;

//     // @PostMapping("/upload")
//     // public String uploadFile(@RequestParam(value = "file", required = false) String file) {
//     //     return "Upload endpoint hit successfully!";
//     // }

//     @PostMapping("/upload")
//     public String uploadFile(@RequestParam("file") MultipartFile file) {
//         try {
//             // Convert MultipartFile to File
//             File convertedFile = new File(file.getOriginalFilename());
//             file.transferTo(convertedFile);  // Save MultipartFile as File

//             nextcloudService.uploadFile(convertedFile);
//             return "File uploaded successfully!";
//         } catch (IOException e) {
//             e.printStackTrace();
//             return "File upload failed!";
//         }
//     }

//     @GetMapping("/download/{fileName}")
//     public File downloadFile(@PathVariable String fileName) {
//         try {
//             return nextcloudService.downloadFile(fileName);
//         } catch (IOException e) {
//             e.printStackTrace();
//             return null;
//         }
//     }
// }
package com.nexgen.nexgen_document_digitizer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nexgen.nexgen_document_digitizer.service.NextcloudService;

@RestController
@RequestMapping("/nextcloud")
public class NextcloudController {

    @Autowired
    private NextcloudService nextcloudService;

    @Value("${nextcloud.url}")
    private String nextcloudUrl;

    @Value("${nextcloud.username}")
    private String username;

    @Value("${nextcloud.password}")
    private String password;

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
