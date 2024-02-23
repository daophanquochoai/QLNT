package com.CNPM.QLNT.services.Impl;

import com.CNPM.QLNT.model.customer;
import com.CNPM.QLNT.repository.CustomerRepository;
import com.CNPM.QLNT.services.Inter.ICustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService implements ICustomerService {
    private final CustomerRepository customerRepository;

    @Override
    public List<customer> getAllCustomer() {
        return customerRepository.findAll();
    }

    @Override
    public Optional<customer> getCustomer(int cus_id) {
        return Optional.of(customerRepository.findById(cus_id).get());
    }

    @Override
    public customer getAdmin() {
        List<customer> list = customerRepository.findAll();
        Optional<customer> adminOptional = list.stream()
                .filter(c -> c.getUA().getAuth_id().getRole().equals("ADMIN"))
                .findFirst();

        return adminOptional.orElse(null); // Trả về null nếu không tìm thấy admin
    }

}
