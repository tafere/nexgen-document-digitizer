package com.nexgen.nexgen_document_digitizer.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;

@Entity
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Auto-generate ID
    private Long id;

    private String fileName;              // Document name uploaded to Nextcloud after uploading the file
    private String contentType;       // MIME type of the document
        
    @Lob  // Used to store large binary data (like files)
    private byte[] content;
     private Long fileSize; // Store file size in bytes

    @CreationTimestamp
    @Column(updatable = false) // Prevent manual updates
    private LocalDateTime uploadedDate; // Auto-inserted by DB

    @ManyToOne  // Link the document to a user (or another entity, like client, patient, etc.)
    @JoinColumn(name = "person_id", nullable = false)
    @JsonBackReference  // Prevent infinite recursion
    private Person person;                // This associates the document with a user entity

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
    
    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public LocalDateTime getUploadedDate() {
        return uploadedDate;
    }

    public void setUploadedDate(LocalDateTime uploadedDate) {
        this.uploadedDate = uploadedDate;
    }
}
