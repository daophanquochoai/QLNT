package com.CNPM.QLNT.repository;

import com.CNPM.QLNT.model.Services;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRepo extends JpaRepository<Services, Integer> {
}
