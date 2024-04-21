package com.CNPM.QLNT.services.Impl;

import com.CNPM.QLNT.exception.ResourceNotFoundException;
import com.CNPM.QLNT.model.Contract;
import com.CNPM.QLNT.model.Customer;
import com.CNPM.QLNT.model.HistoryCustomer;
import com.CNPM.QLNT.model.Room;
import com.CNPM.QLNT.repository.ContractRepo;
import com.CNPM.QLNT.repository.CustomerRepository;
import com.CNPM.QLNT.repository.HistoryCustomerRepo;
import com.CNPM.QLNT.repository.RoomRepo;
import com.CNPM.QLNT.response.InfoContract;
import com.CNPM.QLNT.services.Inter.IContracService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ContractService implements IContracService {
    private final ContractRepo contractRepo;
    private final CustomerRepository customerRepo;
    private final RoomRepo roomRepo;
    private final HistoryCustomerRepo historyCustomerRepo;

    @Override
    public List<Contract> getAllContract() {
        return contractRepo.findAll();
    }

    @Override
    public Contract getContractById(Integer id) {
        log.info("{}",contractRepo.getContractById(id));
        Optional<Contract> c = Optional.ofNullable(contractRepo.getContractById(id));
        if( c.isEmpty()) throw new ResourceNotFoundException("Khong tim thay hop dong");
        return c.get();
    }

    @Override
    public void saveContract(Integer customerId, Integer roomId, InfoContract infoContract) {
        Optional<Customer> customer = customerRepo.findById(customerId);
        if( customer.isEmpty()) throw new ResourceNotFoundException("Khong tim thay khach hang");
        Optional<Room> room = roomRepo.findById(roomId);
        if( room.isEmpty() ) throw new ResourceNotFoundException("Khong tim thay phong");
        Contract contract = new Contract();
        if( infoContract.getBeginDate() == null ) throw new ResourceNotFoundException("beginDate");
        if( infoContract.getCreatedDate() == null ) throw new ResourceNotFoundException("createDate");
        if( infoContract.getEndDate() == null ) throw new ResourceNotFoundException("endDate");
        if( infoContract.getBeginDate().isAfter(infoContract.getCreatedDate())) throw new ResourceNotFoundException("beginDate");
        if( infoContract.getCreatedDate().isAfter(infoContract.getEndDate())) throw new ResourceNotFoundException("createDate");
        if( contractRepo.getContractsByCusIdAndStatus(customerId, true).isPresent()) throw new ResourceNotFoundException("cusId");
        if( room.get().getLimit() < historyCustomerRepo.getCustomersByRoom(room.get().getRoomId()).size()) throw new ResourceNotFoundException("Room");
        Optional<HistoryCustomer> h = historyCustomerRepo.getHistoryCustomerByCustomerId(customerId);
        if( h.isPresent() ){
            // neu dang o chuyen qua phong moi va ghi lai
            h.get().setEndDate(LocalDate.now());
            h.get().setRoomNew(room.get());
            historyCustomerRepo.save(h.get());
        }
        // tao ban ghi phong moi
        HistoryCustomer h1 = new HistoryCustomer();
        h1.setBeginDate(LocalDate.now());
        h1.setCustomer(customer.get());
        h1.setRoomOld(room.get());

        // tao hop dong
        contract.setCustomer(customer.get());
        contract.setEndDate(infoContract.getEndDate());
        contract.setCreatedDate(infoContract.getCreatedDate());
        contract.setBeginDate(infoContract.getBeginDate());
        contract.setRoom(room.get());
        contract.setStatus(true);

        historyCustomerRepo.save(h1);
        contractRepo.save(contract);
    }
}
