package com.CNPM.QLNT.repository;

import com.CNPM.QLNT.model.RoomService;
import com.CNPM.QLNT.response.InfoService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface RoomServiceRepo extends JpaRepository<RoomService, Integer> {
    @Query("select new com.CNPM.QLNT.response.InfoService(r.service.serviceName, r.service.price, r.quantity) " +
            "from RoomService r where r.room.roomId = :roomId and r.beginDate < :date " +
            "and ( r.endDate > :date or r.endDate is null )")
    List<InfoService> getAllServiceByRoomId(Integer roomId, LocalDate date);
}
