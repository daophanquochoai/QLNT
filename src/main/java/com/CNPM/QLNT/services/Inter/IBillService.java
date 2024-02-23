package com.CNPM.QLNT.services.Inter;

import com.CNPM.QLNT.model.bill;

import java.util.List;
import java.util.Optional;

public interface IBillService {
    List<bill> getAllBill();
    Optional<bill> getBill(int room_id);
}
