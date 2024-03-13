package com.CNPM.QLNT.repository;

import com.CNPM.QLNT.model.Communication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommunicationRepo extends JpaRepository<Communication, Integer> {
    @Query("select c from Communication c where c.receiverID IS NULL or c.receiverID.customerId = :id order by c.messageID.createdDatatime")
    List<Communication> getAllNotiecBySender(int id);
    @Query("select c from Communication c where c.receiverID.customerId = :id order by c.messageID.createdDatatime")
    List<Communication> getRequest(int id);
}
