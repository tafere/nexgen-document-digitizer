package com.nexgen.nexgen_document_digitizer.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nexgen.nexgen_document_digitizer.model.Document;
import com.nexgen.nexgen_document_digitizer.service.DocumentService;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/documents")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    // Upload document and associate it with a user
    @PostMapping("/upload")
    public ResponseEntity<Document> uploadDocument(@RequestParam Long personId, @RequestParam MultipartFile file) throws IOException {
        Document uploadedDocument = documentService.uploadDocument(personId, file);
        return new ResponseEntity<>(uploadedDocument, HttpStatus.CREATED);
    }

    @GetMapping("/{documentId}")
    public Document getDocumentById(@PathVariable Long documentId) {
    return documentService.viewDocument(documentId);  // Use the existing viewDocument method
    }

    @GetMapping
public ResponseEntity<List<Document>> getDocumentsByPersonId(@RequestParam Long personId) {
    List<Document> documents = documentService.getDocumentsByPersonId(personId);
    return ResponseEntity.ok(documents);
}


    // Download a document by its ID
    @GetMapping("/download/{documentId}")
    public ResponseEntity<byte[]> downloadDocument(@PathVariable Long documentId) {
        Document document = documentService.viewDocument(documentId);
    
        if (document == null) {
            return ResponseEntity.notFound().build();
        }


        byte[] documentData = documentService.downloadDocument(documentId);

         // Ensure the content type is correct
    String contentType = document.getContentType();
    String fileExtension = getFileExtensionBasedOnContentType(contentType);
        
        // Set headers for downloading the document
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.parseMediaType(contentType));  // Use the actual content type
    // headers.setContentDispositionFormData("attachment", document.getFileName() + "." + fileExtension);

    // Set the Content-Disposition with the appropriate file extension
    headers.setContentDispositionFormData("attachment", document.getFileName() + "." + fileExtension);

    return new ResponseEntity<>(documentData, headers, HttpStatus.OK);
    }

@GetMapping("/view/{documentId}")
public ResponseEntity<byte[]> viewDocument(@PathVariable Long documentId) {
    Document document = documentService.viewDocument(documentId);

    if (document == null) {
        return ResponseEntity.notFound().build();
    }

    byte[] documentData = documentService.downloadDocument(documentId);
    String contentType = document.getContentType();

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.parseMediaType(contentType));

    // ✅ Proper inline display without forcing download
    headers.setContentDisposition(ContentDisposition.inline().build());

    return new ResponseEntity<>(documentData, headers, HttpStatus.OK);
}

    // Helper method to get the file extension based on Content-Type
private String getFileExtensionBasedOnContentType(String contentType) {
    switch (contentType) {
        case "image/png":
            return "png";
        case "application/pdf":
            return "pdf";
        case "text/csv":
            return "csv";
        default:
            return "bin";  // Default to binary for unsupported types
    }
}

    // Delete a document by its ID
    @DeleteMapping("/{documentId}")
    public ResponseEntity<Void> deleteDocument(@PathVariable Long documentId) {
        documentService.deleteDocument(documentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
