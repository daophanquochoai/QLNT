package com.CNPM.QLNT.services;

import com.CNPM.QLNT.model.customer;

import java.util.List;
import java.util.Optional;

public interface ICustomerService {
    public List<customer> getAllCustomer();
    public Optional<customer> getCustomer(int cus_id);
}
