package com.CNPM.QLNT.repository;

import com.CNPM.QLNT.model.Requests;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface RequestRepo extends JpaRepository<Requests, Integer> {
    @Query("select r from Requests r where r.senOrRei = false and r.status = :status")
    List<Requests> getRequestOfCustomerByStatus(boolean status);
    @Query("select r from Requests r where r.senOrRei = false")
    List<Requests> getAllRequestOfCustomer();
    @Query("select r from Requests r where r.senOrRei = true and (r.customer is null or r.customer.customerId = :cus)")
    List<Requests> getAllRequestOfAdmin(Integer cus);
}
