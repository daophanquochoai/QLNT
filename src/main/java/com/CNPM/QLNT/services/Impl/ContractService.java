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
import com.CNPM.QLNT.services.Inter.IContractService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ContractService implements IContractService {
    private final ContractRepo contractRepo;
    private final CustomerRepository customerRepo;
    private final RoomRepo roomRepo;
    private final HistoryCustomerRepo historyCustomerRepo;

    @Override
    public List<Contract> getAllContract() {
        return contractRepo.getAllContract();
    }

    @Override
    public Contract getContractByCustomerId(Integer customerId) {
        log.info("{}",contractRepo.getContractByCustomerId(customerId));
        Optional<Contract> c = Optional.ofNullable(contractRepo.getContractByCustomerId(customerId));
        if( c.isEmpty()) throw new ResourceNotFoundException("Không tìm thấy hợp đồng");
        return c.get();
    }

    @Override
    public void saveContract(Integer customerId, Integer roomId, InfoContract infoContract) {
        Optional<Customer> customer = customerRepo.findById(customerId);
        if( customer.isEmpty()) throw new ResourceNotFoundException("Không tìm thấy khách thuê");
        Optional<Room> room = roomRepo.findById(roomId);
        if( room.isEmpty() ) throw new ResourceNotFoundException("Không tìm thấy phòng");
        Contract contract = new Contract();
        if( infoContract.getBeginDate() == null ) throw new ResourceNotFoundException("Ngày bắt đầu không hợp lệ");
        if( infoContract.getCreatedDate() == null ) throw new ResourceNotFoundException("Ngày tạo không hợp lệ");
        if( infoContract.getEndDate() == null ) throw new ResourceNotFoundException("Ngày kết thúc không hợp lệ");
        if( infoContract.getBeginDate().isBefore(infoContract.getCreatedDate())) throw new ResourceNotFoundException("Ngày bắt đầu không hợp lệ");
        if( infoContract.getEndDate().isBefore(infoContract.getBeginDate())) throw new ResourceNotFoundException("Ngày kết thúc không hợp lệ");
        if( contractRepo.getContractsByCusIdAndStatus(customerId, true).isPresent()) throw new ResourceNotFoundException("Không tìm thấy khách thuê");
        if( room.get().getLimit() < historyCustomerRepo.getCustomersByRoomId(room.get().getRoomId()).size()) throw new ResourceNotFoundException("Phòng đã đầy");
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

    @Override
    public Optional<Contract> getContractByRoomId(Integer roomId) {
        return contractRepo.getContractsByRoomId(roomId);
    }
}
