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
    @Query("SELECT c FROM Customers c WHERE c.room.id = :rd")
    List<Customers> getCustomerByRoomId(@Param("rd") int rd);

    @Query("select new com.CNPM.QLNT.response.InfoLogin(c.userAuthId.authId.role, c.customerId, concat(c.firstName,' ',c.lastName) ) from Customers c where c.userAuthId.usersId.username = :name")
    InfoLogin getLogin(String name);
    @Query("select c from Customers c where  c.userAuthId.authId.role = 'ADMIN'")
    Customers getAdmin();
}
