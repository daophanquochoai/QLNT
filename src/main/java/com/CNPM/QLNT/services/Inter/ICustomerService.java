package com.CNPM.QLNT.services.Inter;

import com.CNPM.QLNT.model.Customer;
import com.CNPM.QLNT.response.InfoLogin;
import com.CNPM.QLNT.response.InfoUser;

import java.util.List;
import java.util.Optional;

public interface ICustomerService {
    List<InfoUser> getAllCustomer();
    Optional<Customer> getCustomer(int cus_id);
    List<InfoUser> getCustomerByRoomId(Integer room_id);
    Boolean addCustomer(InfoUser info) throws Exception;
    void updateCustomer(int id, InfoUser info);
    void deleteCustomer(int id);
    InfoLogin getLogin(String name);
    void updatePassword(String password,Integer customerId);
}
