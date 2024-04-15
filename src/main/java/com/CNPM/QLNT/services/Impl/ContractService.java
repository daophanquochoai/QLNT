package com.CNPM.QLNT.services.Impl;

import com.CNPM.QLNT.exception.ResourceNotFoundException;
import com.CNPM.QLNT.model.Contracts;
import com.CNPM.QLNT.model.Customers;
import com.CNPM.QLNT.model.Room;
import com.CNPM.QLNT.repository.ContractRepo;
import com.CNPM.QLNT.repository.CustomerRepository;
import com.CNPM.QLNT.repository.RoomRepo;
import com.CNPM.QLNT.response.InfoContract;
import com.CNPM.QLNT.services.Inter.IContracService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ContractService implements IContracService {
    private final ContractRepo contractRepo;
    private final CustomerRepository customerRepo;
    private final RoomRepo roomRepo;

    @Override
    public List<Contracts> getAllContract() {
        return contractRepo.findAll();
    }

    @Override
    public Contracts getContractById(Integer id) {
        log.info("{}",contractRepo.getContractById(id));
        Optional<Contracts> c = Optional.ofNullable(contractRepo.getContractById(id));
        if( c.isEmpty()) throw new ResourceNotFoundException("Khong tim thay hop dong");
        return c.get();
    }

    @Override
    public void saveContract(Integer customerId, Integer roomId, InfoContract infoContract) {
        Optional<Customers> customer = customerRepo.findById(customerId);
        if( customer.isEmpty()) throw new ResourceNotFoundException("Khong tim thay khach hang");
        Optional<Room> room = roomRepo.findById(roomId);
        if( room.isEmpty() ) throw new ResourceNotFoundException("Khong tim thay phong");
        Contracts contract = new Contracts();
        if( infoContract.getBeginDate() == null ) throw new ResourceNotFoundException("beginDate");
        if( infoContract.getConDate() == null ) throw new ResourceNotFoundException("conDate");
        if( infoContract.getEndDate() == null ) throw new ResourceNotFoundException("endDate");
        if( infoContract.getBeginDate().isAfter(infoContract.getConDate())) throw new ResourceNotFoundException("beginDate");
        if( infoContract.getConDate().isAfter(infoContract.getEndDate())) throw new ResourceNotFoundException("conDate");
//        if( contractRepo.getContractsByCusIdAndStatus(customerId, true).isPresent())
    }
}
