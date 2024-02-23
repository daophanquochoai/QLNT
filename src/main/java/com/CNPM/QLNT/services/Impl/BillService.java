package com.CNPM.QLNT.services.Impl;

import com.CNPM.QLNT.model.bill;
import com.CNPM.QLNT.repository.BillRepo;
import com.CNPM.QLNT.services.Inter.IBillService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BillService implements IBillService {
    private final BillRepo billRepo;
    @Override
    public List<bill> getAllBill() {
        return billRepo.findAll();
    }

    @Override
    public Optional<bill> getBill(int room_id) {
        return Optional.of(billRepo.findBillByRoomId(room_id));
    }
}
