package com.CNPM.QLNT.repository;

import com.CNPM.QLNT.model.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ContractRepo extends JpaRepository<Contract, Integer> {
    @Query("select c from Contract c where c.cusId.customerId = :id")
    Contract getContractById(Integer id);

    @Query("select c from Contract c where c.cusId.customerId = :CusId and c.status = :status")
    Optional<Contract> getContractsByCusIdAndStatus(Integer CusId, Boolean status);
}
