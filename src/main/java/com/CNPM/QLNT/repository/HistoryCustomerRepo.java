package com.CNPM.QLNT.repository;

import com.CNPM.QLNT.model.Customers;
import com.CNPM.QLNT.model.HistoryCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface HistoryCustomerRepo extends JpaRepository<HistoryCustomer, Integer> {

//    @Query("select count(h.customers) from HistoryCustomer h where h.roomNew = :roomId and h.movingDate is null")
//    public Integer getNumberCustomerInRoom(Integer roomId);
    @Query("select h.customers from HistoryCustomer h where h.roomOld.id = :roomId and h.endDate is null and h.roomNew is null")
    public List<Customers> getCustmersByRoom(Integer roomId);
}
