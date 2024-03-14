package com.CNPM.QLNT.repository;

import com.CNPM.QLNT.model.Customers;
import com.CNPM.QLNT.response.InfoLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customers, Integer> {
    @Query("SELECT c FROM Customers c WHERE c.Room.id = :rd")
    List<Customers> getCustomerByRoomId(@Param("rd") int rd);

    @Query("select new com.CNPM.QLNT.response.InfoLogin(c.customerId,CONCAT(c.firstName, ' ', c.lastName)) from Customers c where c.userAuthId.usersId.username = :name")
    InfoLogin getLogin(String name);
}
