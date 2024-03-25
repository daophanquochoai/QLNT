package com.CNPM.QLNT.repository;

import com.CNPM.QLNT.model.Auth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface authRepo extends JpaRepository<Auth, Integer> {
    @Query("SELECT a FROM Auth a WHERE a.role = 'USER'")
    Auth getAuth();
}
