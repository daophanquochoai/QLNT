package com.CNPM.QLNT.repository;

import com.CNPM.QLNT.model.Customers;
import com.CNPM.QLNT.response.InfoLogin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customers, Integer> {
    @Query("select new com.CNPM.QLNT.response.InfoLogin(c.userAuthId.role, c.customerId, concat(c.firstName,' ',c.lastName) ) from Customers c where c.userAuthId.username = :name")
    InfoLogin getLogin(String name);
    @Query("select c from Customers c where c.userAuthId.id = :id")
    Customers getInfoCustomerr(Integer id);
    Optional<Customers> findByCCCD(String CCCD);
}
