package com.CNPM.QLNT.services.Impl;

import com.CNPM.QLNT.model.communication;
import com.CNPM.QLNT.model.customer;
import com.CNPM.QLNT.model.requests;
import com.CNPM.QLNT.repository.CommunicationRepo;
import com.CNPM.QLNT.repository.RequestRepo;
import com.CNPM.QLNT.services.Inter.ICommuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommuService implements ICommuService {
    private final CommunicationRepo communicationRepo;
    private final RequestRepo requestRepo;
    @Override
    public void addMessage(requests mess_id, customer sender_id, customer receiver_id) {
        communication com = new communication();
        com.setReceiverID(receiver_id);
        com.setSenderId(sender_id);
        com.setMessageID(mess_id);
        requestRepo.save(mess_id);
        communicationRepo.save(com);
    }
}
