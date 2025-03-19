package com.nexgen.nexgen_document_digitizer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nexgen.nexgen_document_digitizer.model.Document;

public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findByPersonId(Long personId);

}
