package com.CNPM.QLNT.repository;

import com.CNPM.QLNT.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepo extends JpaRepository<Room, Integer> {
    @Query("select r from Room r where r.status = :status")
    List<Room> getRoomByStatus(boolean status);
}
