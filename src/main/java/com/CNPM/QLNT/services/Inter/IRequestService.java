package com.CNPM.QLNT.services.Inter;

import com.CNPM.QLNT.model.Requests;
import java.util.List;

public interface IRequestService {
    void addRequest_DonGia(Requests re);
    List<Requests> getAllRequestOfCustomerByStatus(boolean status);
    List<Requests> getAllRequestOfCustomer();
    void updateRequest(int id);
    void deleteCommunication(int id);
    List<Requests> getNoticeBySender( Integer id);
}
