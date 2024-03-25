package com.CNPM.QLNT.repository;

import com.CNPM.QLNT.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomResitory extends JpaRepository<Room, Integer> {
    @Query("select r from Room r where r.status = :status")
    List<Room> getRoomByStatus(boolean status);
    @Query("select  r from Room r where SIZE(r.customers) > 0 and SIZE(r.customers) < r.limit")
    List<Room> getRoomByThueTrong();
    @Query("select  r from Room r where SIZE(r.customers) = 0")
    List<Room> getRoomByTrong();
    @Query("select  r from Room r where SIZE(r.customers) = r.limit")
    List<Room> getRoomByDay();
}
