package com.CNPM.QLNT.services;

import com.CNPM.QLNT.model.customer;
import com.CNPM.QLNT.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService implements ICustomerService{
    private final CustomerRepository customerRepository;

    @Override
    public List<customer> getAllCustomer() {
        return customerRepository.findAll();
    }

    @Override
    public Optional<customer> getCustomer(int cus_id) {
        return Optional.of(customerRepository.findById(cus_id).get());
    }
}
