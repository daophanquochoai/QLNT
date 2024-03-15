package com.CNPM.QLNT.services.Impl;

import com.CNPM.QLNT.exception.ResourceNotFoundException;
import com.CNPM.QLNT.model.Bill;
import com.CNPM.QLNT.repository.BillRepo;
import com.CNPM.QLNT.response.BIllInRoom;
import com.CNPM.QLNT.response.Report;
import com.CNPM.QLNT.response.RoomRes;
import com.CNPM.QLNT.services.Inter.IBillService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BillService implements IBillService {
    private final BillRepo billRepo;
    @Override
    public List<Bill> getAllBill() {
        return billRepo.findAll();
    }

    @Override
    public Report getReport(int month, int year) {
        try{
            List<Bill> list = getAllBill();
            Report r = new Report();
            list = list.stream().filter( b -> (b.getBeginDate().getMonth() == month && b.getBeginDate().getYear() == year && b.getStatus() == Boolean.FALSE)).collect(Collectors.toList());
            List<RoomRes> listR = (List<RoomRes>) list.stream().map(c -> {
                return new RoomRes(c.getRoomId().getId(), c.getRoomId().getLimit(), c.getRoomId().getHomeCategoryId().getHome_category_name(), c.getRoomId().getPrice(), c.getRoomId().getStatus());
            });
            r.setListRoom(listR);
            Double total = list.stream().filter(b->b.getStatus()==Boolean.TRUE).mapToDouble(bill -> (bill.getWaterNumberEnd() - bill.getWaterNumberBegin()) * bill.getPriceQuotationId().getWaterPrice())
                    .sum();
            r.setMoney(total);
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
                        BigInteger.valueOf(b.getPriceQuotationId().getElectricityPrice() *(b.getElectricNumberEnd()-b.getElectricNumberBegin())
                        + b.getPriceQuotationId().getWaterPrice() * (b.getWaterNumberEnd()-b.getWaterNumberBegin()) + b.getOtherPrice())
                );
                br.setGhi_Chu(b.getGhiChu());
                br.setDong_tien( b.getStatus());
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
                    BigInteger.valueOf(b.getPriceQuotationId().getElectricityPrice() *(b.getElectricNumberEnd()-b.getElectricNumberBegin())
                            + b.getPriceQuotationId().getWaterPrice() * (b.getWaterNumberEnd()-b.getWaterNumberBegin()) + b.getOtherPrice())
            );
            br.setGhi_Chu(b.getGhiChu());
            br.setDong_tien( b.getStatus());
            listBR.add(br);
        });
        return listBR;
    }
}
