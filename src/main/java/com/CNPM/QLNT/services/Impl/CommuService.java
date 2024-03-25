package com.CNPM.QLNT.services.Impl;

import com.CNPM.QLNT.exception.ResourceNotFoundException;
import com.CNPM.QLNT.model.Communication;
import com.CNPM.QLNT.model.Customers;
import com.CNPM.QLNT.model.Requests;
import com.CNPM.QLNT.repository.CommunicationRepo;
import com.CNPM.QLNT.repository.RequestRepo;
import com.CNPM.QLNT.response.Message;
import com.CNPM.QLNT.services.Inter.ICommuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommuService implements ICommuService {
    private final CommunicationRepo communicationRepo;
    private final RequestRepo requestRepo;
    @Override
    public void addMessage(Requests mess_id, Customers sender_id, Customers receiver_id) {
        if( receiver_id != null){
            if( sender_id.getCustomerId() == receiver_id.getCustomerId()){
                throw new ResourceNotFoundException("I can't send myself");
            }
        }
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
    public List<Message> getRequest(int id, boolean status) {
        Optional<List<Communication>>  list = Optional.ofNullable(communicationRepo.getRequest(id, status));
        if( list.isEmpty()) throw new ResourceNotFoundException("Not Found");
        return list.get().stream().map(m -> {
            Message message = new Message(m.getCommunicationID() ,m.getSenderId(), m.getReceiverID(), m.getMessageID().getMessage(), m.getMessageID().getCreatedDatatime());
            return message;
        }).collect(Collectors.toList());
    }

    @Override
    public void updateRequest(int id) {
        Optional<Communication> communication = communicationRepo.findById(id);
        if( communication.isEmpty())    throw new ResourceNotFoundException("Request can't found");
        communication.get().getMessageID().setStatus(false);
        communicationRepo.save(communication.get());
    }

    @Override
    public List<Communication> getAll() {
        return communicationRepo.findAll();
    }

    @Override
    public void deleteCommunication(int id){
        try{
            Communication c = communicationRepo.findById(id).get();
            if( !c.getMessageID().getStatus() ){
                requestRepo.deleteById(c.getMessageID().getRequestsId());
                communicationRepo.deleteById(id);
            }else {
                throw new ResourceNotFoundException("It wasn't commited");
            }
        }catch (Exception ex){
            throw new ResourceNotFoundException(ex.getMessage());
        }
    }
}
