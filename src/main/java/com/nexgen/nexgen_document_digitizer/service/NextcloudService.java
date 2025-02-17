package com.nexgen.nexgen_document_digitizer.service;
// import java.io.IOException;

// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.http.HttpEntity;
// import org.springframework.http.HttpHeaders;
// import org.springframework.http.HttpMethod;
// import org.springframework.http.ResponseEntity;
// import org.springframework.stereotype.Service;
// import org.springframework.web.client.RestTemplate;
// import org.springframework.web.multipart.MultipartFile;

// @Service
// public class NextcloudService {

//     @Value("${nextcloud.url}")
//     private String nextcloudUrl;

//     @Value("${nextcloud.username}")
//     private String username;

//     @Value("${nextcloud.password}")
//     private String password;

//     public String uploadFile(MultipartFile file) throws IOException {
//         // String uploadUrl = nextcloudUrl + "/remote.php/dav/files/" + username + "/upload-folder/" + file.getOriginalFilename();
//         // String uploadUrl = nextcloudUrl + "/remote.php/dav/files/" + username + "/" + file.getName();
//         String fileName = file.getOriginalFilename(); // Get the original filename
//         if (fileName == null || fileName.isEmpty()) {
//             fileName = "default_filename"; // Set a default filename if it's missing
//         }
        
//         // String uploadUrl = nextcloudUrl + "/remote.php/dav/files/" + username + "/" + fileName;
//         String uploadUrl = "http://localhost:8080/remote.php/dav/files/admin/Auto_Insurance_1.png"; // Correct URL

//         System.out.println("Uploading to: " + uploadUrl);



//         // Prepare the file content for upload
//         byte[] fileBytes = file.getBytes();

//         // Prepare headers
//         HttpHeaders headers = new HttpHeaders();
//         headers.set("Authorization", "Basic " + java.util.Base64.getEncoder().encodeToString((username + ":" + password).getBytes()));

//         // Prepare request entity
//         HttpEntity<byte[]> entity = new HttpEntity<>(fileBytes, headers);

//         // Create a RestTemplate instance to send the request
//         RestTemplate restTemplate = new RestTemplate();

//         try {
//             ResponseEntity<String> response = restTemplate.exchange(uploadUrl, HttpMethod.PUT, entity, String.class);

//             if (response.getStatusCode().is2xxSuccessful()) {
//                 return "File uploaded successfully!";
//             } else {
//                 return "Failed to upload file. Response code: " + response.getStatusCode();
//             }
//         } catch (Exception e) {
//             e.printStackTrace();
//             return "Error uploading file: " + e.getMessage();
//         }
//     }
// }

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Service
public class NextcloudService {

    @Value("${nextcloud.url}")
    private String nextcloudUrl;

    @Value("${nextcloud.username}")
    private String username;

    @Value("${nextcloud.password}")
    private String password;

    public String uploadFile(MultipartFile file) throws IOException {
        String uploadUrl = nextcloudUrl + "/remote.php/dav/files/" + username +"/"+ file.getOriginalFilename();
        System.out.println("Uploading to: " + uploadUrl);

        // Prepare the file content for upload
        byte[] fileBytes = file.getBytes();

        // Create headers with Basic Authentication
        HttpHeaders headers = new HttpHeaders();
        String auth = username + ":" + password;
        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes());
        String authHeader = "Basic " + new String(encodedAuth);
        headers.set("Authorization", authHeader);
        headers.set("Content-Type", "application/octet-stream");

        // Create the request entity
        HttpEntity<byte[]> requestEntity = new HttpEntity<>(fileBytes, headers);

        // Create a RestTemplate instance
        RestTemplate restTemplate = new RestTemplate();

        // Send the PUT request
        ResponseEntity<String> response = restTemplate.exchange(uploadUrl, HttpMethod.PUT, requestEntity, String.class);

        // Check the response status
        if (response.getStatusCode().is2xxSuccessful()) {
            return "File uploaded successfully";
        } else {
            return "Failed to upload file: " + response.getStatusCode();
        }
    }
}