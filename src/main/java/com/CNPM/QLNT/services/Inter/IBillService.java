package com.CNPM.QLNT.services.Inter;

import com.CNPM.QLNT.model.Bill;
import com.CNPM.QLNT.response.BillInRoom;
import com.CNPM.QLNT.response.DetailBill;
import com.CNPM.QLNT.response.Report;

import java.util.List;

public interface IBillService {
    List<Bill> getAllBill();
    Report getReport( int month, int year);
    List<BillInRoom> getAllBillByRoom(int room);
    List<BillInRoom> getAllBillByStatus(int room, boolean status);
    Long getRevenue(int year);
    void billCalculator( BillInRoom bill);
    DetailBill getBillByRoomInMonthInYear( Integer roomId, Integer Month, Integer Year);
}
