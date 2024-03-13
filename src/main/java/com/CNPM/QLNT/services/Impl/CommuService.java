package com.CNPM.QLNT.services.Impl;

import com.CNPM.QLNT.exception.ResourceNotFoundException;
import com.CNPM.QLNT.model.Communication;
import com.CNPM.QLNT.model.Customers;
import com.CNPM.QLNT.model.Requests;
import com.CNPM.QLNT.repository.CommunicationRepo;
import com.CNPM.QLNT.repository.RequestRepo;
import com.CNPM.QLNT.services.Inter.ICommuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommuService implements ICommuService {
    private final CommunicationRepo communicationRepo;
    private final RequestRepo requestRepo;
    @Override
    public void addMessage(Requests mess_id, Customers sender_id, Customers receiver_id) {
        Communication com = new Communication();
        com.setReceiverID(receiver_id);
        com.setSenderId(sender_id);
        com.setMessageID(mess_id);
        communicationRepo.save(com);
    }

    @Override
    public List<Communication> getNoticeBySender(int id) {
        Optional<List<Communication>>  list = Optional.ofNullable(communicationRepo.getAllNotiecBySender(id));
        if( list.isEmpty()) throw new ResourceNotFoundException("Not Found");
        return list.get();
    }

    @Override
    public List<Communication> getRequest(int id) {
        Optional<List<Communication>>  list = Optional.ofNullable(communicationRepo.getRequest(id));
        if( list.isEmpty()) throw new ResourceNotFoundException("Not Found");
        return list.get();
    }
}
