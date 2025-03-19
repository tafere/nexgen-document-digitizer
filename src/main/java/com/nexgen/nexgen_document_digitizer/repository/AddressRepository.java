package com.nexgen.nexgen_document_digitizer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nexgen.nexgen_document_digitizer.model.Address;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
}
