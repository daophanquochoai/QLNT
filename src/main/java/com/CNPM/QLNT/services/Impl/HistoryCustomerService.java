package com.CNPM.QLNT.services.Impl;

import com.CNPM.QLNT.exception.ResourceNotFoundException;
import com.CNPM.QLNT.repository.HistoryCustomerRepo;
import com.CNPM.QLNT.response.History;
import com.CNPM.QLNT.services.Inter.IHistoryCustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HistoryCustomerService implements IHistoryCustomerService {
    private final HistoryCustomerRepo historyCustomerRepo;
    @Override
    public List<History> getAllHistoryCustomer() {
        List<History> list = historyCustomerRepo.findAll().stream().map( h ->{
            History history = new History();
            if( h.getBeginDate() != null){
                history.setStayDay(h.getBeginDate());
            }else{
                throw new ResourceNotFoundException("StayDay");
            }
            if(h.getEndDate() == null){
                history.setMoveDay(null);
                if( h.getRoomNew() != null){
                    throw new ResourceNotFoundException("MoveDay");
                }else{
                    history.setNewRoom(null);
                }
            }else {
                if( h.getEndDate().isAfter(h.getBeginDate())){
                    history.setMoveDay(h.getEndDate());
                }else{
                    throw new ResourceNotFoundException("MoveDay");
                }
            }
            if( h.getRoomOld() != null){
                history.setOldRoom(h.getRoomOld());
            }else throw new ResourceNotFoundException("OldRoom");
            return history;
        }).collect(Collectors.toList());
        return list;
    }
}
