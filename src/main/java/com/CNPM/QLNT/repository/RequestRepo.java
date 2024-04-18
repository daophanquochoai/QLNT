package com.CNPM.QLNT.repository;

import com.CNPM.QLNT.model.Requests;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface RequestRepo extends JpaRepository<Requests, Integer> {
    @Query("select r from Requests r where r.isSend = false and r.status = :status")
    List<Requests> getRequestOfCustomerByStatus(boolean status);
    @Query("select r from Requests r where r.isSend = false")
    List<Requests> getAllRequestOfCustomer();
    @Query("select r from Requests r where r.isSend = true and (r.customerId is null or r.customerId.customerId = :cus)")
    List<Requests> getAllRequestOfAdmin(Integer cus);
}
