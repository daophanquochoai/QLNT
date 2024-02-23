package com.CNPM.QLNT.repository;

import com.CNPM.QLNT.model.requests;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestRepo extends JpaRepository<requests, Integer> {
}
