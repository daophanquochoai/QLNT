package com.CNPM.QLNT.repository;

import com.CNPM.QLNT.model.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ContractRepo extends JpaRepository<Contract, Integer> {
    @Query("select c from Contract c where c.customer.customerId = :customerId")
    Contract getContractById(Integer customerId);

    @Query("select c from Contract c where c.customer.customerId = :customerId and c.status = :status")
    Optional<Contract> getContractsByCusIdAndStatus(Integer customerId, Boolean status);
    @Query("select  c from Contract c where c.room.roomId = :roomId")
    Optional<Contract> getContractsByRoomID(Integer roomId);
}
