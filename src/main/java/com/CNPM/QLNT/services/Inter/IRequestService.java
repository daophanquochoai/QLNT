package com.CNPM.QLNT.services.Inter;

import com.CNPM.QLNT.model.Request;
import java.util.List;

public interface IRequestService {
    void addRequest_DonGia(Request re);
    List<Request> getAllRequestOfCustomerByStatus(boolean status);
    List<Request> getAllRequestOfCustomer();
    void updateRequest(int id);
    void deleteCommunication(int id);
    List<Request> getNoticeBySender(Integer id);
}
