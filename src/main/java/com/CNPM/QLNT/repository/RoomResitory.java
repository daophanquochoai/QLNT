package com.CNPM.QLNT.repository;

import com.CNPM.QLNT.model.room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomResitory extends JpaRepository<room, Integer> {
}
