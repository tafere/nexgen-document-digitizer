package com.nexgen.nexgen_document_digitizer.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nexgen.nexgen_document_digitizer.model.Address;
import com.nexgen.nexgen_document_digitizer.model.Person;
import com.nexgen.nexgen_document_digitizer.service.AddressService;
import com.nexgen.nexgen_document_digitizer.service.PersonService;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/users")
public class PersonController {

    @Autowired
    private PersonService personService;

    @Autowired
    private AddressService addressService;

    // Get a user by ID
    @GetMapping("/{userId}")
    public ResponseEntity<Person> getUserById(@PathVariable Long userId) {
        try {
            Person person = personService.getUserById(userId);
            return new ResponseEntity<>(person, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Create a new user with address
    // @PostMapping
    // public ResponseEntity<Person> createUser(@RequestBody Person person) {
    //     try {
    //         if (person.getAddress() != null) {
    //             Address address = person.getAddress();
    //             // Create the address if not already in the database
    //             if (address.getId() == null) {
    //                 address = addressService.saveAddress(address); // Assuming AddressService handles address creation
    //             }
    //             person.setAddress(address);
    //         }
    
    //         Person newPerson = personService.createUser(person);
    //         return new ResponseEntity<>(newPerson, HttpStatus.CREATED);
    //     } catch (RuntimeException e) {
    //         return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    //     }
    // }
    
@PostMapping
public ResponseEntity<Person> createUser(@RequestBody Person person) {
    System.out.println("Received person: " + person); // Log the received person

    try {
        if (person.getAddress() != null) {
            Address address = person.getAddress(); // Retrieve the Address object from the request

            // You can add any additional logic to check if the address exists, or is new
            // For example, if address needs to be saved independently, you could do that here:
            if (address.getId() == null) {
                // Save address if it's a new one (you might want to check if it's already in the database)
                addressService.saveAddress(address); // Assuming you have a repository for Address
            }

                      // After saving address, set it to the person
                      person.setAddress(address);  // Ensure the address is set to the person

                      // Now save the person
                      Person savedPerson = personService.createUser(person);
                      return new ResponseEntity<>(savedPerson, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // If no address is provided, return bad request
    } catch (Exception e) {
        // Handle any errors
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}

// @PostMapping
// public ResponseEntity<String> createUser(@RequestBody Person person) {
//     System.out.println("Received person object: " + person);
//     return ResponseEntity.ok("User created successfully");
// }

    // Update an existing user
    @PutMapping("/{userId}")
    public ResponseEntity<Person> updateUser(@PathVariable Long userId, @RequestBody Person userDetails) {
        try {
            Person updatedPerson = personService.updateUser(userId, userDetails);
            return new ResponseEntity<>(updatedPerson, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Delete a user by ID
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        try {
            personService.deleteUser(userId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Search persons by name or email
    @GetMapping("/search")
    public List<Person> searchPersons(@RequestParam String search) {
        return personService.searchPersons(search);
    }
}

