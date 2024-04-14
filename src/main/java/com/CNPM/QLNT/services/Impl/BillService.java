package com.CNPM.QLNT.services.Impl;

import com.CNPM.QLNT.exception.ResourceNotFoundException;
import com.CNPM.QLNT.model.Bill;
import com.CNPM.QLNT.model.ElectricPrice;
import com.CNPM.QLNT.model.WaterPrice;
import com.CNPM.QLNT.repository.BillRepo;
import com.CNPM.QLNT.repository.ElectricPriceRepo;
import com.CNPM.QLNT.repository.WaterPriceRepo;
import com.CNPM.QLNT.response.BIllInRoom;
import com.CNPM.QLNT.response.PriceQuotation;
import com.CNPM.QLNT.response.Report;
import com.CNPM.QLNT.response.RoomRes;
import com.CNPM.QLNT.services.Inter.IBillService;
import com.CNPM.QLNT.services.Inter.IDonGiaService;
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
//    private final ElectricPriceRepo electricPriceRepo;
//    private final WaterPriceRepo waterPriceRepo;
    private final IDonGiaService iDonGiaService;
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
                br.setElectricNumberBegin(b.getElectricNumberBegin());
                br.setElectricNumberEnd(b.getElectricNumberEnd());
                br.setNumberBill(b.getBillId());
                br.setBeginDate(b.getBeginDate());
                br.setEndDate(b.getEndDate());
                br.setWaterNumberBegin(b.getWaterNumberBegin());
                br.setWaterNumberEnd(b.getWaterNumberEnd());
                br.setGhiChu(b.getGhiChu());
                br.setTotal(
                    b.getTotal()
                );
                br.setDong_tien( b.getStatus());
                br.setRoomId(b.getRoomId().getId());
                return br;
            }).collect(Collectors.toList());
            r.setChuaDong(listCD);

            l = list.stream().filter( b -> (b.getBeginDate().getMonth().getValue() == month && b.getBeginDate().getYear() == year && b.getStatus() == Boolean.TRUE)).collect(Collectors.toList());
            List<BIllInRoom> listDD = (List<BIllInRoom>) l.stream().map(b -> {
                BIllInRoom br = new BIllInRoom();
                br.setElectricNumberBegin(b.getElectricNumberBegin());
                br.setElectricNumberEnd(b.getElectricNumberEnd());
                br.setNumberBill(b.getBillId());
                br.setBeginDate(b.getBeginDate());
                br.setEndDate(b.getEndDate());
                br.setWaterNumberBegin(b.getWaterNumberBegin());
                br.setWaterNumberEnd(b.getWaterNumberEnd());
                br.setGhiChu(b.getGhiChu());
                br.setTotal(
                        b.getTotal()
                );
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
                br.setElectricNumberBegin(b.getElectricNumberBegin());
                br.setElectricNumberEnd(b.getElectricNumberEnd());
                br.setNumberBill(b.getBillId());
                br.setBeginDate(b.getBeginDate());
                br.setEndDate(b.getEndDate());
                br.setWaterNumberBegin(b.getWaterNumberBegin());
                br.setWaterNumberEnd(b.getWaterNumberEnd());
                br.setGhiChu(b.getGhiChu());
                br.setTotal(
                        b.getTotal()
                );
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
            br.setElectricNumberBegin(b.getElectricNumberBegin());
            br.setElectricNumberEnd(b.getElectricNumberEnd());
            br.setNumberBill(b.getBillId());
            br.setBeginDate(b.getBeginDate());
            br.setEndDate(b.getEndDate());
            br.setWaterNumberBegin(b.getWaterNumberBegin());
            br.setWaterNumberEnd(b.getWaterNumberEnd());
            br.setGhiChu(b.getGhiChu());
            br.setTotal(
                    b.getTotal()
            );
            br.setDong_tien( b.getStatus());
            br.setRoomId(b.getRoomId().getId());
            listBR.add(br);
        });
        return listBR;
    }

    @Override
    public Long getDoanhThu(int year) {
        return billRepo.getBillByYear(year)
                .stream()
                .mapToLong(bill -> {
                    return bill.getTotal();
                })
                .sum();
    }

    @Override
    public void billCalculator(BIllInRoom bill) {
        try{
            Bill b = new Bill();
            if( bill.getBeginDate() == null ){
                throw new ResourceNotFoundException("beginDate");
            }else{
                b.setBeginDate(bill.getBeginDate());
            }
            if( bill.getEndDate() == null){
                throw new ResourceNotFoundException("endDate");
            }else{
                if( bill.getEndDate().isAfter(bill.getBeginDate())){
                    b.setEndDate(bill.getEndDate());
                }else{
                    throw new ResourceNotFoundException("endDate");
                }
            }
            if( bill.getElectricNumberBegin() >= 0){
                b.setElectricNumberBegin(bill.getElectricNumberBegin());
            }
            if( bill.getElectricNumberEnd() > bill.getElectricNumberBegin()){
                b.setElectricNumberEnd(bill.getElectricNumberEnd());
            }else {
                throw new ResourceNotFoundException("electricNumberEnd");
            }
            if( bill.getWaterNumberBegin() >= 0){
                b.setWaterNumberBegin(bill.getWaterNumberBegin());
            }
            if( bill.getElectricNumberEnd() > bill.getWaterNumberBegin()){
                b.setWaterNumberEnd(bill.getWaterNumberEnd());
            }else {
                throw new ResourceNotFoundException("electricNumberEnd");
            }
            b.setGhiChu(bill.getGhiChu());
            Integer numberElec = bill.getElectricNumberEnd() - bill.getElectricNumberBegin();
            Integer numberWater = bill.getWaterNumberEnd() - bill.getWaterNumberBegin();
            PriceQuotation price = iDonGiaService.getDonGiaNow();

        }catch (Exception ex){

        }
    }
}
