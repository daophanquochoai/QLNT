package com.CNPM.QLNT.repository;

import com.CNPM.QLNT.model.auth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface authRepo extends JpaRepository<auth, Integer> {
    @Query("SELECT a FROM auth a WHERE a.id = 2")
    auth getAuth();
}
