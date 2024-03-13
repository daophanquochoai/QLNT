package com.CNPM.QLNT.repository;

import com.CNPM.QLNT.model.PriceQuotation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface donGiaRepository extends JpaRepository<PriceQuotation, Integer> {
}
