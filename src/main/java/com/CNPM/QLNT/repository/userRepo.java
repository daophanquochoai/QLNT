package com.CNPM.QLNT.repository;

import com.CNPM.QLNT.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface userRepo extends JpaRepository<Users, Integer> {
}
