package com.CNPM.QLNT.repository;

import com.CNPM.QLNT.model.PriceQuotation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface donGiaRepository extends JpaRepository<PriceQuotation, Integer> {
    @Query("select p from PriceQuotation p order by p.id desc, p.timeChange desc ")
    List<PriceQuotation> findAllByOrderByTimeChangeDesc();
}
