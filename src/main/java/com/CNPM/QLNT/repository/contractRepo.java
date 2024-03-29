package com.CNPM.QLNT.repository;

import com.CNPM.QLNT.model.Contracts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface contractRepo extends JpaRepository<Contracts, Integer> {
    @Query("select c from Contracts c where c.cusId.customerId = :id")
    Contracts getContractById(int id);
}
