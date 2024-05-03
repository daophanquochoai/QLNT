package com.CNPM.QLNT.repository;

import com.CNPM.QLNT.model.Customer;
import com.CNPM.QLNT.model.HistoryCustomer;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface HistoryCustomerRepo extends JpaRepository<HistoryCustomer, Integer> {

    @Query("select h from HistoryCustomer h where h.customer.customerId = :CustomerId and h.endDate is null and h.roomNew is null")
    Optional<HistoryCustomer> getHistoryCustomerByCustomerId(Integer CustomerId);
    // lay tat ca khach thue cua 1 phong
    @Query("select h.customer from HistoryCustomer h where h.roomOld.roomId = :roomId and h.endDate is null and h.roomNew is null")
    public List<Customer> getCustomersByRoomId(Integer roomId);
}
