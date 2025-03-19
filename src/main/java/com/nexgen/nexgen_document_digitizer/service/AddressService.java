package com.nexgen.nexgen_document_digitizer.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nexgen.nexgen_document_digitizer.model.Address;
import com.nexgen.nexgen_document_digitizer.repository.AddressRepository;

@Service
public class AddressService {

    private final AddressRepository addressRepository;

    @Autowired
    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public List<Address> getAllAddresses() {
        return addressRepository.findAll();
    }

    public Optional<Address> getAddressById(Long id) {
        return addressRepository.findById(id);
    }

    public Address saveAddress(Address address) {
        return addressRepository.save(address);
    }

    public Address updateAddress(Long id, Address addressDetails) {
        return addressRepository.findById(id).map(address -> {
            address.setAddress1(addressDetails.getAddress1());
            address.setAddress2(addressDetails.getAddress2());
            address.setCity(addressDetails.getCity());
            address.setState(addressDetails.getState());
            address.setCountry(addressDetails.getCountry());
            return addressRepository.save(address);
        }).orElseThrow(() -> new RuntimeException("Address not found with id: " + id));
    }

    public void deleteAddress(Long id) {
        addressRepository.deleteById(id);
    }
}
