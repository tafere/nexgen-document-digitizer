package com.nexgen.nexgen_document_digitizer.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nexgen.nexgen_document_digitizer.model.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {
    // You can define custom query methods here if needed

    // For example, a method to find a user by email:
    Person findByEmail(String email);
    
 @Query("SELECT p FROM Person p LEFT JOIN FETCH p.documents WHERE p.firstName LIKE %:search% OR p.lastName LIKE %:search% OR p.email LIKE %:search%")
    List<Person> searchPersonsWithDocuments(@Param("search") String search);
}
