package com.CNPM.QLNT.repository;

import com.CNPM.QLNT.model.bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BillRepo extends JpaRepository<bill, Integer> {
}
