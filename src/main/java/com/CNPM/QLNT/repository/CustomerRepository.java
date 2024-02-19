package com.CNPM.QLNT.repository;

import com.CNPM.QLNT.model.customer;
import org.springframework.data.jpa.repository.JpaRepository;
public interface CustomerRepository extends JpaRepository<customer, Integer> {
}
