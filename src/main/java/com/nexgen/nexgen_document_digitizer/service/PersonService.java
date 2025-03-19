package com.nexgen.nexgen_document_digitizer.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nexgen.nexgen_document_digitizer.model.Address;
import com.nexgen.nexgen_document_digitizer.model.Person;
import com.nexgen.nexgen_document_digitizer.repository.AddressRepository;
import com.nexgen.nexgen_document_digitizer.repository.PersonRepository;

@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private AddressRepository addressRepository;

    // Create a new user with associated address
    public Person createUser(Person person) {
        Address address = person.getAddress();
        
        // If the address is new, save it
        if (address != null) {
            address = addressRepository.save(address);
            person.setAddress(address); // Associate address with the person
        }

        // Save the person with the associated address
        return personRepository.save(person);
    }

    // Update an existing user
    public Person updateUser(Long userId, Person userDetails) {
        Optional<Person> existingUserOpt = personRepository.findById(userId);
        if (existingUserOpt.isPresent()) {
            Person existingPerson = existingUserOpt.get();
            existingPerson.setFirstName(userDetails.getFirstName());
            existingPerson.setLastName(userDetails.getLastName());
            existingPerson.setDateOfBirth(userDetails.getDateOfBirth());

            // Check if the address is updated
            if (userDetails.getAddress() != null) {
                Address address = userDetails.getAddress();
                address = addressRepository.save(address); // Save or update the address
                existingPerson.setAddress(address); // Set the updated address
            }

            return personRepository.save(existingPerson);
        } else {
            throw new RuntimeException("Person not found");
        }
    }

    // Get a user by ID
    public Person getUserById(Long userId) {
        Optional<Person> user = personRepository.findById(userId);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new RuntimeException("User not found");
        }
    }

    // Delete a user by ID
    public void deleteUser(Long userId) {
        Optional<Person> existingUserOpt = personRepository.findById(userId);
        if (existingUserOpt.isPresent()) {
            personRepository.delete(existingUserOpt.get());
        } else {
            throw new RuntimeException("User not found");
        }
    }

    // Search person by first name, last name, or email
    public List<Person> searchPersons(String search) {
        return personRepository.searchPersonsWithDocuments(search);
    }
}
