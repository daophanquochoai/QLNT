package com.CNPM.QLNT.repository;

import com.CNPM.QLNT.model.Requests;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestRepo extends JpaRepository<Requests, Integer> {
}
