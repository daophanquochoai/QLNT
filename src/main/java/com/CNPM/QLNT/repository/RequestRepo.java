package com.CNPM.QLNT.repository;

import com.CNPM.QLNT.model.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface RequestRepo extends JpaRepository<Request, Integer> {
    @Query("select r from Request r where r.isSend = false and r.status = :status")
    List<Request> getRequestOfCustomerByStatus(boolean status);
    @Query("select r from Request r where r.isSend = false")
    List<Request> getAllRequestOfCustomer();
    @Query("select r from Request r where r.isSend = true and (r.customerId is null or r.customerId.customerId = :cus)")
    List<Request> getAllRequestOfAdmin(Integer cus);
}
