package com.CNPM.QLNT.repository;

import com.CNPM.QLNT.model.Customers;
import com.CNPM.QLNT.model.HistoryCustomer;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface HistoryCustomerRepo extends JpaRepository<HistoryCustomer, Integer> {

    @Query("select h from HistoryCustomer h where h.customerId.customerId = :CustomerId and h.endDate is null and h.roomNew is null")
    Optional<HistoryCustomer> getHistoryCustomerByCustomerId(Integer CustomerId);
    // lay khạch hang 1 phong
    @Query("select h.customerId from HistoryCustomer h where h.roomOld.roomId = :roomId and h.endDate is null and h.roomNew is null")
    public List<Customers> getCustmersByRoom(Integer roomId);
}
