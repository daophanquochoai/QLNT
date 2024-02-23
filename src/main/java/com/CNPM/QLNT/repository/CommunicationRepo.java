package com.CNPM.QLNT.repository;

import com.CNPM.QLNT.model.communication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunicationRepo extends JpaRepository<communication, Integer> {
}
