package com.nexgen.nexgen_document_digitizer.service;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
        String fileName = file.getOriginalFilename();
        String uploadUrl = nextcloudUrl + "/remote.php/dav/files/" + username +"/"+fileName ;
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
            return fileName;  // or return uploadUrl;
        } else {
            return "Failed to upload file: " + response.getStatusCode();
        }
    }

        // Method to download file from Nextcloud by file ID
        public byte[] downloadFile(String fileName) {
            String downloadUrl = nextcloudUrl + "/remote.php/dav/files/" + username + "/" + fileName;
            System.out.println("Downloading from: " + downloadUrl);
        
            // Create headers with Basic Authentication
            HttpHeaders headers = new HttpHeaders();
            String auth = username + ":" + password;
            byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes());
            String authHeader = "Basic " + new String(encodedAuth);
            headers.set("Authorization", authHeader);
        
            // Create a RestTemplate instance
            RestTemplate restTemplate = new RestTemplate();
        
            // Create the request entity
            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        
            // Send the GET request
            ResponseEntity<byte[]> response = restTemplate.exchange(downloadUrl, HttpMethod.GET, requestEntity, byte[].class);
        
            // Check the response status
            if (response.getStatusCode().is2xxSuccessful()) {
                return response.getBody();
            } else {
                throw new RuntimeException("Failed to download file: " + response.getStatusCode());
            }
        }
    

  // 🔹 View File (Stream to Browser)
  public ResponseEntity<byte[]> viewFile(String fileName) {
    String fileUrl = nextcloudUrl + "/remote.php/dav/files/" + username + "/" + fileName;
    System.out.println("Fetching file for viewing: " + fileUrl);

    HttpHeaders headers = new HttpHeaders();
    String auth = username + ":" + password;
    byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes());
    headers.set("Authorization", "Basic " + new String(encodedAuth));

    RestTemplate restTemplate = new RestTemplate();
    HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

    ResponseEntity<byte[]> response = restTemplate.exchange(fileUrl, HttpMethod.GET, requestEntity, byte[].class);

    if (response.getStatusCode().is2xxSuccessful()) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.parseMediaType(determineContentType(fileName)));
        return new ResponseEntity<>(response.getBody(), responseHeaders, HttpStatus.OK);
    } else {
        return ResponseEntity.status(response.getStatusCode()).build();
    }
}

// 🔹 Determine Content-Type based on file extension
private String determineContentType(String fileName) {
    if (fileName.endsWith(".pdf")) return "application/pdf";
    if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) return "image/jpeg";
    if (fileName.endsWith(".png")) return "image/png";
    if (fileName.endsWith(".txt")) return "text/plain";
    return "application/octet-stream";
}

        // Method to delete a file from Nextcloud by file ID
        public void deleteFile(String fileId) {
            // Implement logic to delete the file from Nextcloud
            System.out.println("File deleted from Nextcloud: " + fileId);
        }
}