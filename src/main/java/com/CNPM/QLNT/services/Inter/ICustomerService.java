package com.CNPM.QLNT.services.Inter;

import com.CNPM.QLNT.model.customer;

import java.util.List;
import java.util.Optional;

public interface ICustomerService {
    List<customer> getAllCustomer();
    Optional<customer> getCustomer(int cus_id);
    customer getAdmin();

}
