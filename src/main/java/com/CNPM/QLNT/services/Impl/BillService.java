package com.CNPM.QLNT.services.Impl;

import com.CNPM.QLNT.exception.ResourceNotFoundException;
import com.CNPM.QLNT.model.Bill;
import com.CNPM.QLNT.repository.BillRepo;
import com.CNPM.QLNT.repository.RoomServiceRepo;
import com.CNPM.QLNT.response.*;
import com.CNPM.QLNT.services.Inter.IBillService;
import com.CNPM.QLNT.services.Inter.IPriceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BillService implements IBillService {
    private final BillRepo billRepo;
    private final IPriceService iPriceService;
    private final RoomServiceRepo roomServiceRepo;

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
            List<BillInRoom> unpaidList = (List<BillInRoom>) l.stream().map(b -> {
                BillInRoom br = new BillInRoom();
                br.setElectricNumberBegin(b.getElectricNumberBegin());
                br.setElectricNumberEnd(b.getElectricNumberEnd());
                br.setNumberBill(b.getBillId());
                br.setBeginDate(b.getBeginDate());
                br.setEndDate(b.getEndDate());
                br.setWaterNumberBegin(b.getWaterNumberBegin());
                br.setWaterNumberEnd(b.getWaterNumberEnd());
                br.setNote(b.getNote());
                br.setTotal(
                    b.getTotal()
                );
                br.setPaid( b.getStatus());
                br.setRoomId(b.getRoom().getRoomId());
                return br;
            }).collect(Collectors.toList());
            r.setUnpaidRoomList(unpaidList);

            l = list.stream().filter( b -> (b.getBeginDate().getMonth().getValue() == month && b.getBeginDate().getYear() == year && b.getStatus() == Boolean.TRUE)).collect(Collectors.toList());
            List<BillInRoom> paidList = (List<BillInRoom>) l.stream().map(b -> {
                BillInRoom br = new BillInRoom();
                br.setElectricNumberBegin(b.getElectricNumberBegin());
                br.setElectricNumberEnd(b.getElectricNumberEnd());
                br.setNumberBill(b.getBillId());
                br.setBeginDate(b.getBeginDate());
                br.setEndDate(b.getEndDate());
                br.setWaterNumberBegin(b.getWaterNumberBegin());
                br.setWaterNumberEnd(b.getWaterNumberEnd());
                br.setNote(b.getNote());
                br.setTotal(
                        b.getTotal()
                );
                br.setPaid( b.getStatus());
                br.setRoomId(b.getRoom().getRoomId());
                return br;
            }).collect(Collectors.toList());
            r.setPaidRoomList(paidList);
            return r;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<BillInRoom> getAllBillByRoom(int room) {
            Optional<List<Bill>> listB = Optional.ofNullable(billRepo.getBillByIdRoom(room));
            if( listB.isEmpty()) throw new ResourceNotFoundException("Not found");
            List<BillInRoom> listBR = new ArrayList<>();
            listB.get().stream().forEach(b-> {
                BillInRoom br = new BillInRoom();
                br.setElectricNumberBegin(b.getElectricNumberBegin());
                br.setElectricNumberEnd(b.getElectricNumberEnd());
                br.setNumberBill(b.getBillId());
                br.setBeginDate(b.getBeginDate());
                br.setEndDate(b.getEndDate());
                br.setWaterNumberBegin(b.getWaterNumberBegin());
                br.setWaterNumberEnd(b.getWaterNumberEnd());
                br.setNote(b.getNote());
                br.setTotal(
                        b.getTotal()
                );
                br.setPaid( b.getStatus());
                br.setRoomId(b.getRoom().getRoomId());
                listBR.add(br);
                });
            return listBR;
    }

    @Override
    public List<BillInRoom> getAllBillByStatus(int room, boolean status) {
        Optional<List<Bill>> listB = Optional.ofNullable(billRepo.getBillByStatus(status, room));
        if( listB.isEmpty()) throw new ResourceNotFoundException("Not found");
        List<BillInRoom> listBR = new ArrayList<>();
        listB.get().stream().forEach(b-> {
            BillInRoom br = new BillInRoom();
            br.setElectricNumberBegin(b.getElectricNumberBegin());
            br.setElectricNumberEnd(b.getElectricNumberEnd());
            br.setNumberBill(b.getBillId());
            br.setBeginDate(b.getBeginDate());
            br.setEndDate(b.getEndDate());
            br.setWaterNumberBegin(b.getWaterNumberBegin());
            br.setWaterNumberEnd(b.getWaterNumberEnd());
            br.setNote(b.getNote());
            br.setTotal(
                    b.getTotal()
            );
            br.setPaid(b.getStatus());
            br.setRoomId(b.getRoom().getRoomId());
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
    public void billCalculator(BillInRoom bill) {
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
            b.setNote(bill.getNote());
            Integer numberElec = bill.getElectricNumberEnd() - bill.getElectricNumberBegin();
            Integer numberWater = bill.getWaterNumberEnd() - bill.getWaterNumberBegin();
            PriceQuotation price = iPriceService.getPriceNow();
            List<InfoService> infoService = roomServiceRepo.getAllServiceByRoomId(bill.getRoomId(),bill.getBeginDate());
            Long total = 0L;
            total += numberWater * price.getWaterPrice() + numberElec* price.getElectricPrice();
            total += infoService.stream().mapToLong( s -> s.getQuantity()*s.getPrice()).sum();
            b.setTotal(total);
            billRepo.save(b);
        }catch (Exception ex){

        }
    }
}
