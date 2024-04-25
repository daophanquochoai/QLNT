package com.CNPM.QLNT.services.Impl;

import com.CNPM.QLNT.exception.ResourceNotFoundException;
import com.CNPM.QLNT.model.Bill;
import com.CNPM.QLNT.model.ElectricPrice;
import com.CNPM.QLNT.model.Room;
import com.CNPM.QLNT.model.WaterPrice;
import com.CNPM.QLNT.repository.BillRepo;
import com.CNPM.QLNT.repository.RoomRepo;
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
    private final RoomRepo roomRepo;

    @Override
    public List<Bill> getAllBill() {
        return billRepo.findAll();
    }

    @Override
    public Report getReport(int month, int year) {
        try{
            List<Bill> list = billRepo.getReport(month, year);
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
            Optional<Room> room = roomRepo.findById(bill.getRoomId());
            if( room.isEmpty()) throw new ResourceNotFoundException("roomId");
            b.setNote(bill.getNote());
            int numberElec = bill.getElectricNumberEnd() - bill.getElectricNumberBegin();
            int numberWater = bill.getWaterNumberEnd() - bill.getWaterNumberBegin();
            PriceQuotation price = iPriceService.getPriceNow();
            List<InfoService> infoService = roomServiceRepo.getAllServiceByRoomId(bill.getRoomId(),bill.getBeginDate());
            long total = 0L;
            total += (long) numberWater * price.getWaterPrice() + (long) numberElec * price.getElectricPrice();
            total += infoService.stream().mapToLong( s -> s.getQuantity()*s.getPrice()).sum();

            total += Long.parseLong(String.valueOf(room.get().getPrice()));
            b.setTotal(total);
            billRepo.save(b);
        }catch (Exception ex){

        }
    }

    @Override
    public DetailBill getBillByRoomInMonthInYear(Integer roomId, Integer Month, Integer Year) {
        List<WaterPrice> waterList = iPriceService.getAllWaterPrice();
        List<ElectricPrice> electricList = iPriceService.getAllElectricPrice();
        WaterPrice water = new WaterPrice();
        for( WaterPrice w : waterList ){

            if( w.getChangedDate().getYear() < Year){
                water = w;
                break;
            }else if( w.getChangedDate().getYear() == Year && w.getChangedDate().getMonth().getValue() < Month){
                water = w;
                break;
            }
        }
        ElectricPrice electric = new ElectricPrice();
        for( ElectricPrice e : electricList ){
            if( e.getChangedDate().getYear() < Year){
                electric = e;
                break;
            }else if( e.getChangedDate().getYear() == Year && e.getChangedDate().getMonth().getValue() < Month){
                electric = e;
                break;
            }
        }
        Optional<Bill> bill = billRepo.getBillByRoomInMonthInYear(roomId, Month, Year);
        if( bill.isEmpty()) throw new ResourceNotFoundException("not found");
        List<InfoService> service = roomServiceRepo.getALlServiceByRoomId(roomId, Month, Year);
        DetailBill detailBill = new DetailBill();
        detailBill.setNumberBill(bill.get().getBillId());
        detailBill.setBeginDate(bill.get().getBeginDate());
        detailBill.setEndDate(bill.get().getEndDate());
        detailBill.setElectricNumberBegin(bill.get().getElectricNumberBegin());
        detailBill.setElectricNumberEnd(bill.get().getElectricNumberEnd());
        detailBill.setWaterNumberBegin(bill.get().getWaterNumberBegin());
        detailBill.setWaterNumberEnd(bill.get().getWaterNumberEnd());
        detailBill.setNote(bill.get().getNote());
        detailBill.setTotal(bill.get().getTotal());
        detailBill.setIsPaid(bill.get().getStatus());
        detailBill.setRoomId(roomId);
        detailBill.setWaterPrice(water.getPrice());
        detailBill.setElectronucPrice(electric.getPrice());
        long total = 0L;
        total += (long) detailBill.getWaterPrice() * (bill.get().getWaterNumberEnd() - bill.get().getWaterNumberBegin()) + (long) detailBill.getElectronucPrice() * (bill.get().getElectricNumberEnd()-bill.get().getElectricNumberBegin());
        total += service.stream().mapToLong( s -> s.getQuantity()*s.getPrice()).sum();
        detailBill.setRoomPrice(bill.get().getTotal() - total);
        detailBill.setService(service);
        return detailBill;
    }
}
