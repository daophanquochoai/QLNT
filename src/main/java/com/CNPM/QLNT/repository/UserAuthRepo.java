package com.CNPM.QLNT.repository;

import com.CNPM.QLNT.model.UserAuth;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAuthRepo extends JpaRepository<UserAuth, Integer> {
    Optional<UserAuth> findByUsername(String username);
}
