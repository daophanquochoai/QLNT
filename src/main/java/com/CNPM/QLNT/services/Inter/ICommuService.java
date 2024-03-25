package com.CNPM.QLNT.services.Inter;

import com.CNPM.QLNT.model.Communication;
import com.CNPM.QLNT.model.Customers;
import com.CNPM.QLNT.model.Requests;
import com.CNPM.QLNT.response.Message;

import java.util.List;

public interface ICommuService {
    void addMessage(Requests mess_id, Customers sender_id, Customers receiver_id );
    List<Communication> getNoticeBySender(int id);
    List<Message> getRequest(int id, boolean status);
    void updateRequest(int id);
    List<Communication> getAll();
    void deleteCommunication( int id );
}
