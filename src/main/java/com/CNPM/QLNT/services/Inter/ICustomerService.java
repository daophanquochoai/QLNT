package com.CNPM.QLNT.services.Inter;

import com.CNPM.QLNT.model.customer;
import com.CNPM.QLNT.response.Info_user;

import java.util.List;
import java.util.Optional;

public interface ICustomerService {
    List<Info_user> getAllCustomer();
    Optional<customer> getCustomer(int cus_id);
    customer getAdmin();
    List<Info_user> getCustomerByRoomId(int room_id);
    Boolean addCustomer(Info_user info) throws Exception;
    void updateCustomer(int id, Info_user info);
    void deleteCustomer(int id);
}
