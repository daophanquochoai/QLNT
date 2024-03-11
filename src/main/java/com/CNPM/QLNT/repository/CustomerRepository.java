package com.CNPM.QLNT.repository;

import com.CNPM.QLNT.model.customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<customer, Integer> {
    @Query("SELECT c FROM customer c WHERE c.Room.id = :rd")
    List<customer> getCustomerByRoomId(@Param("rd") int rd);
}
