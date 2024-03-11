package com.CNPM.QLNT.repository;

import com.CNPM.QLNT.model.users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface userRepo extends JpaRepository<users, Integer> {
}
