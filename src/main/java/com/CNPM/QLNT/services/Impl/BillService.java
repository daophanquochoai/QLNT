package com.CNPM.QLNT.services.Impl;

import com.CNPM.QLNT.exception.ResourceNotFoundException;
import com.CNPM.QLNT.model.Bill;
import com.CNPM.QLNT.repository.BillRepo;
import com.CNPM.QLNT.response.BIllInRoom;
import com.CNPM.QLNT.response.Report;
import com.CNPM.QLNT.response.RoomRes;
import com.CNPM.QLNT.services.Inter.IBillService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BillService implements IBillService {
    private final BillRepo billRepo;
    @Override
    public List<Bill> getAllBill() {
        return billRepo.findAll();
    }

    @Override
    public Report getReport(int month, int year) {
        try{
            List<Bill> list = billRepo.getRepost(month, year);
            Report r = new Report();
            List<Bill> l = list.stream().filter( b -> (b.getBeginDate().getMonth().getValue() == month && b.getBeginDate().getYear() == year && b.getStatus() == Boolean.FALSE)).collect(Collectors.toList());
            List<BIllInRoom> listCD = (List<BIllInRoom>) l.stream().map(b -> {
                BIllInRoom br = new BIllInRoom();
                br.setNumber_E_Begin(b.getElectricNumberBegin());
                br.setNumber_E_End(b.getElectricNumberEnd());
                br.setNumberBill(b.getBillId());
                br.setDay_Begin(b.getBeginDate());
                br.setDay_End(b.getEndDate());
                br.setNumber_W_Begin(b.getWaterNumberBegin());
                br.setNumber_W_End(b.getWaterNumberEnd());
                br.setOther_Price(b.getOtherPrice());
                br.setGhi_Chu(b.getGhiChu());
                br.setThanh_Tien(
                        BigInteger.valueOf((long) b.getPriceQuotationId().getElectricityPrice() *(b.getElectricNumberEnd()-b.getElectricNumberBegin())
                                + (long) b.getPriceQuotationId().getWaterPrice() * (b.getWaterNumberEnd()-b.getWaterNumberBegin()) + b.getOtherPrice())
                );
                br.setGhi_Chu(b.getGhiChu());
                br.setDong_tien( b.getStatus());
                br.setRoomId(b.getRoomId().getId());
                return br;
            }).collect(Collectors.toList());
            r.setChuaDong(listCD);

            l = list.stream().filter( b -> (b.getBeginDate().getMonth().getValue() == month && b.getBeginDate().getYear() == year && b.getStatus() == Boolean.TRUE)).collect(Collectors.toList());
            List<BIllInRoom> listDD = (List<BIllInRoom>) l.stream().map(b -> {
                BIllInRoom br = new BIllInRoom();
                br.setNumber_E_Begin(b.getElectricNumberBegin());
                br.setNumber_E_End(b.getElectricNumberEnd());
                br.setNumberBill(b.getBillId());
                br.setDay_Begin(b.getBeginDate());
                br.setDay_End(b.getEndDate());
                br.setNumber_W_Begin(b.getWaterNumberBegin());
                br.setNumber_W_End(b.getWaterNumberEnd());
                br.setOther_Price(b.getOtherPrice());
                br.setGhi_Chu(b.getGhiChu());
                br.setThanh_Tien(
                        BigInteger.valueOf((long) b.getPriceQuotationId().getElectricityPrice() *(b.getElectricNumberEnd()-b.getElectricNumberBegin())
                                + (long) b.getPriceQuotationId().getWaterPrice() * (b.getWaterNumberEnd()-b.getWaterNumberBegin()) + b.getOtherPrice())
                );
                br.setGhi_Chu(b.getGhiChu());
                br.setDong_tien( b.getStatus());
                br.setRoomId(b.getRoomId().getId());
                return br;
            }).collect(Collectors.toList());
            r.setDaDong(listDD);
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<BIllInRoom> getAllBillByRoom(int room) {
            Optional<List<Bill>> listB = Optional.ofNullable(billRepo.getBillByIdRoom(room));
            if( listB.isEmpty()) throw new ResourceNotFoundException("Not found");
            List<BIllInRoom> listBR = new ArrayList<>();
            listB.get().stream().forEach(b-> {
                BIllInRoom br = new BIllInRoom();
                br.setNumber_E_Begin(b.getElectricNumberBegin());
                br.setNumber_E_End(b.getElectricNumberEnd());
                br.setNumberBill(b.getBillId());
                br.setDay_Begin(b.getBeginDate());
                br.setDay_End(b.getEndDate());
                br.setNumber_W_Begin(b.getWaterNumberBegin());
                br.setNumber_W_End(b.getWaterNumberEnd());
                br.setOther_Price(b.getOtherPrice());
                br.setGhi_Chu(b.getGhiChu());
                br.setThanh_Tien(
                        BigInteger.valueOf((long) b.getPriceQuotationId().getElectricityPrice() *(b.getElectricNumberEnd()-b.getElectricNumberBegin())
                        + (long) b.getPriceQuotationId().getWaterPrice() * (b.getWaterNumberEnd()-b.getWaterNumberBegin()) + b.getOtherPrice())
                );
                br.setGhi_Chu(b.getGhiChu());
                br.setDong_tien( b.getStatus());
                br.setRoomId(b.getRoomId().getId());
                listBR.add(br);
                });
            return listBR;
    }

    @Override
    public List<BIllInRoom> getAllBillByStatus(int room, boolean status) {
        Optional<List<Bill>> listB = Optional.ofNullable(billRepo.getBillByStatus(status, room));
        if( listB.isEmpty()) throw new ResourceNotFoundException("Not found");
        List<BIllInRoom> listBR = new ArrayList<>();
        listB.get().stream().forEach(b-> {
            BIllInRoom br = new BIllInRoom();
            br.setNumber_E_Begin(b.getElectricNumberBegin());
            br.setNumber_E_End(b.getElectricNumberEnd());
            br.setNumberBill(b.getBillId());
            br.setDay_Begin(b.getBeginDate());
            br.setDay_End(b.getEndDate());
            br.setNumber_W_Begin(b.getWaterNumberBegin());
            br.setNumber_W_End(b.getWaterNumberEnd());
            br.setOther_Price(b.getOtherPrice());
            br.setGhi_Chu(b.getGhiChu());
            br.setThanh_Tien(
                    BigInteger.valueOf((long) b.getPriceQuotationId().getElectricityPrice() *(b.getElectricNumberEnd()-b.getElectricNumberBegin())
                            + (long) b.getPriceQuotationId().getWaterPrice() * (b.getWaterNumberEnd()-b.getWaterNumberBegin()) + b.getOtherPrice())
            );
            br.setGhi_Chu(b.getGhiChu());
            br.setDong_tien( b.getStatus());
            br.setRoomId(b.getRoomId().getId());
            listBR.add(br);
        });
        return listBR;
    }
}
