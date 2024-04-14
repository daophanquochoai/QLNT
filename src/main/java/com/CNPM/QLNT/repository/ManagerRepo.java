package com.CNPM.QLNT.repository;

import com.CNPM.QLNT.model.Manager;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ManagerRepo extends JpaRepository<Manager, Integer> {
    @Query("select m from Manager m where m.userAuthId.id = :id")
    Manager getInfoManager(Integer id);
    Optional<Manager> findByCCCD(String CCCD);
}
