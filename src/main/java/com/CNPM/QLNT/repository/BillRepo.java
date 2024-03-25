package com.CNPM.QLNT.repository;

import com.CNPM.QLNT.model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillRepo extends JpaRepository<Bill, Integer> {
    @Query("select b from Bill b where b.roomId.id = :room")
    List<Bill> getBillByIdRoom(int room);

    @Query("select b from Bill b where b.status = :status and b.roomId.id = :room")
    List<Bill> getBillByStatus(boolean status, int room);

    @Query("select  b from Bill b where MONTH(b.beginDate)= :month and YEAR(b.beginDate) = :year")
    List<Bill> getRepost(int month, int year);

    @Query("select b from Bill  b where YEAR(b.beginDate) = :year")
    List<Bill> getBillByYear( int year);
}
