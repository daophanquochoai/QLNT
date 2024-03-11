package com.CNPM.QLNT.repository;

import com.CNPM.QLNT.model.room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomResitory extends JpaRepository<room, Integer> {
    @Query("select r from room r where r.status = :status")
    List<room> getRoomByStatus(boolean status);
}
