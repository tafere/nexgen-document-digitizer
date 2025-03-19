package com.nexgen.nexgen_document_digitizer.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.nexgen.nexgen_document_digitizer.model.Document;
import com.nexgen.nexgen_document_digitizer.model.Person;
import com.nexgen.nexgen_document_digitizer.repository.DocumentRepository;
import com.nexgen.nexgen_document_digitizer.repository.PersonRepository;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private NextcloudService nextcloudService;

    // Method to upload a document and associate it with a user
    public Document uploadDocument(Long userId, MultipartFile file) throws IOException {
        // Upload file to Nextcloud and get the fileId
        String nextcloudFileName= nextcloudService.uploadFile(file);

        // Create a new Document object and set properties
        Document document = new Document();
        document.setContentType(file.getContentType());
        document.setFileName(nextcloudFileName);  // Store Nextcloud file ID
        document.setFileSize(file.getSize());     // Store file size in bytes
        document.setUploadedDate(LocalDateTime.now()); // Store current timestamp

        // Retrieve the user and associate the document with the user
        Person person = personRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Person not found"));
        document.setPerson(person);

        // Save the document to the database
        return documentRepository.save(document);
    }

    // Method to get document metadata
    public Document viewDocument(Long documentId) {
        return documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document not found with id: " + documentId));
    }

    public List<Document> getDocumentsByPersonId(Long personId) {
        return documentRepository.findByPersonId(personId);
    }
    

    // Method to download a document by ID
    public byte[] downloadDocument(Long documentId) {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document not found with id: " + documentId));
        
        // Fetch the document content from Nextcloud
        return nextcloudService.downloadFile(document.getFileName());
    }

    // Method to delete a document by ID
    public void deleteDocument(Long documentId) {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document not found with id: " + documentId));

        // Delete the document from the repository
        documentRepository.delete(document);

        // Also delete the file from Nextcloud
        nextcloudService.deleteFile(document.getFileName());
    }
}
