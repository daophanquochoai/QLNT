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
import com.CNPM.QLNT.services.Inter.IRoomService;
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
    private final IRoomService iRoomService;
    private final RoomServiceRepo roomServiceRepo;
    private final RoomRepo roomRepo;

    @Override
    public List<Bill> getAllBill() {
        return billRepo.findAll();
    }

    @Override
    public Report getReport(int month, int year) {
        try {
            List<Bill> list = billRepo.getReport(month, year);
            Report r = new Report();
            List<Bill> l = list.stream().filter(b -> (b.getStatus() == Boolean.FALSE)).collect(Collectors.toList());
            List<BillInRoom> unpaidList = (List<BillInRoom>) l.stream().map(b -> {
                System.out.println(b);
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
                return br;
            }).collect(Collectors.toList());
            r.setUnpaidRoomList(unpaidList);

            l = list.stream().filter(b -> (b.getStatus() == Boolean.TRUE)).collect(Collectors.toList());
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
                br.setPaid(b.getStatus());
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
    public List<BillInRoom> getAllBillByRoomId(int roomId) {
        Optional<List<Bill>> listB = Optional.ofNullable(billRepo.getBillByRoomId(roomId));
        if (listB.isEmpty()) throw new ResourceNotFoundException("Không tìm thấy hóa đơn");
        List<BillInRoom> listBR = new ArrayList<>();
        listB.get().stream().forEach(b -> {
            BillInRoom br = new BillInRoom();
            br.setElectricNumberBegin(b.getElectricNumberBegin());
            br.setElectricNumberEnd(b.getElectricNumberEnd());
            br.setNumberBill(b.getBillId());
            br.setBeginDate(b.getBeginDate());
            br.setEndDate(b.getEndDate());
            br.setWaterNumberBegin(b.getWaterNumberBegin());
            br.setWaterNumberEnd(b.getWaterNumberEnd());
            br.setNote(b.getNote());
            br.setTotal(b.getTotal());
            br.setPaid(b.getStatus());
            br.setRoomId(b.getRoom().getRoomId());
            listBR.add(br);
        });
        return listBR;
    }

    @Override
    public List<BillInRoom> getAllBillByStatus(int room, boolean status) {
        Optional<List<Bill>> listB = Optional.ofNullable(billRepo.getBillByStatus(status, room));
        if (listB.isEmpty()) throw new ResourceNotFoundException("Không tìm thấy hóa đơn");
        List<BillInRoom> listBR = new ArrayList<>();
        listB.get().stream().forEach(b -> {
            BillInRoom br = new BillInRoom();
            br.setElectricNumberBegin(b.getElectricNumberBegin());
            br.setElectricNumberEnd(b.getElectricNumberEnd());
            br.setNumberBill(b.getBillId());
            br.setBeginDate(b.getBeginDate());
            br.setEndDate(b.getEndDate());
            br.setWaterNumberBegin(b.getWaterNumberBegin());
            br.setWaterNumberEnd(b.getWaterNumberEnd());
            br.setNote(b.getNote());
            br.setTotal(b.getTotal());
            br.setPaid(b.getStatus());
            br.setRoomId(b.getRoom().getRoomId());
            listBR.add(br);
        });
        return listBR;
    }

    @Override
    public Long getRevenue(int year) {
        return billRepo.getBillByYear(year)
                .stream()
                .mapToLong(bill -> {
                    return bill.getTotal();
                })
                .sum();
    }

    @Override
    public void addBill(BillInRoom billInRoom) {
        try {
            Bill b = new Bill();
            if (billInRoom.getBeginDate() == null) {
                throw new ResourceNotFoundException("Ngày bắt đầu không hợp lệ");
            } else {
                b.setBeginDate(billInRoom.getBeginDate());
            }
            if (billInRoom.getEndDate() == null) {
                throw new ResourceNotFoundException("Ngày kết thúc không hợp lệ");
            } else {
                if (billInRoom.getEndDate().isAfter(billInRoom.getBeginDate())) {
                    b.setEndDate(billInRoom.getEndDate());
                } else {
                    throw new ResourceNotFoundException("Ngày kết thúc không hợp lệ");
                }
            }
            if (billInRoom.getElectricNumberBegin() >= 0) {
                b.setElectricNumberBegin(billInRoom.getElectricNumberBegin());
            } else throw new ResourceNotFoundException("1");
            if (billInRoom.getElectricNumberEnd() >= billInRoom.getElectricNumberBegin()) {
                b.setElectricNumberEnd(billInRoom.getElectricNumberEnd());
            } else {
                throw new ResourceNotFoundException("Số điện kết thúc không hợp lệ");
            }
            if (billInRoom.getWaterNumberBegin() >= 0) {
                b.setWaterNumberBegin(billInRoom.getWaterNumberBegin());
            } else throw new ResourceNotFoundException("2");
            if (billInRoom.getWaterNumberEnd() >= billInRoom.getWaterNumberBegin()) {
                b.setWaterNumberEnd(billInRoom.getWaterNumberEnd());
            } else {
                throw new ResourceNotFoundException("Số nước kết thúc không hợp lệ");
            }
            Optional<Room> room = iRoomService.getRoomByRoomId(billInRoom.getRoomId());
            if (room.isEmpty()) throw new ResourceNotFoundException("Không tìm thấy phòng");
            else b.setRoom(room.get());
            b.setNote(billInRoom.getNote());
            b.setTotal(billInRoom.getTotal());
            b.setStatus(false);
            billRepo.save(b);
        } catch (Exception ex) {
            throw new ResourceNotFoundException("Dữ liệu sai");
        }
    }

    @Override
    public DetailBill getBillByRoomInMonthInYear(Integer roomId, Integer Month, Integer Year) {
        List<WaterPrice> waterList = iPriceService.getAllWaterPrice();
        List<ElectricPrice> electricList = iPriceService.getAllElectricPrice();
        WaterPrice water = new WaterPrice();
        for (WaterPrice w : waterList) {

            if (w.getChangedDate().getYear() < Year) {
                water = w;
                break;
            } else if (w.getChangedDate().getYear() == Year && w.getChangedDate().getMonth().getValue() < Month) {
                water = w;
                break;
            }
        }
        ElectricPrice electric = new ElectricPrice();
        for (ElectricPrice e : electricList) {
            if (e.getChangedDate().getYear() < Year) {
                electric = e;
                break;
            } else if (e.getChangedDate().getYear() == Year && e.getChangedDate().getMonth().getValue() < Month) {
                electric = e;
                break;
            }
        }
        Optional<Bill> bill = billRepo.getBillByRoomInMonthInYear(roomId, Month, Year);
        if (bill.isEmpty()) throw new ResourceNotFoundException("Không tìm thấy hóa đơn");
        List<InfoService> service = roomServiceRepo.getAllServiceByRoomIdMonthYear(roomId, Month, Year);
        DetailBill detailBill = new DetailBill();
        detailBill.setBillId(bill.get().getBillId());
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
        detailBill.setElectricPrice(electric.getPrice());
        long total = 0L;
        total += (long) detailBill.getWaterPrice() * (bill.get().getWaterNumberEnd() - bill.get().getWaterNumberBegin()) + (long) detailBill.getElectricPrice() * (bill.get().getElectricNumberEnd() - bill.get().getElectricNumberBegin());
        total += service.stream().mapToLong(s -> s.getQuantity() * s.getPrice()).sum();
        detailBill.setRoomPrice(bill.get().getTotal() - total);
        detailBill.setService(service);
        return detailBill;
    }

    @Override
    public InfoInvoice getInfoToAddInvoice(Integer roomId, Integer Month, Integer Year) {
        List<WaterPrice> waterList = iPriceService.getAllWaterPrice();
        List<ElectricPrice> electricList = iPriceService.getAllElectricPrice();
        WaterPrice water = new WaterPrice();
        for (WaterPrice w : waterList) {
            if (w.getChangedDate().getYear() < Year) {
                water = w;
                break;
            } else if (w.getChangedDate().getYear() == Year && w.getChangedDate().getMonth().getValue() < Month) {
                water = w;
                break;
            }
        }
        ElectricPrice electric = new ElectricPrice();
        for (ElectricPrice e : electricList) {
            if (e.getChangedDate().getYear() < Year) {
                electric = e;
                break;
            } else if (e.getChangedDate().getYear() == Year && e.getChangedDate().getMonth().getValue() < Month) {
                electric = e;
                break;
            }
        }
        InfoInvoice infoInvoice = new InfoInvoice();
        Optional<Room> room = iRoomService.getRoomByRoomId(roomId);
        Optional<Bill> bill = billRepo.getBillByRoomInMonthInYear(roomId, Month - 1, Year);
        List<InfoService> service = roomServiceRepo.getAllServiceByRoomIdMonthYear(roomId, Month, Year);
        if (bill.isEmpty()) {
            infoInvoice.setElectricNumberBegin(0);
            infoInvoice.setWaterNumberBegin(0);
        } else {
            infoInvoice.setElectricNumberBegin(bill.get().getElectricNumberEnd());
            infoInvoice.setWaterNumberBegin(bill.get().getWaterNumberEnd());
        }
        infoInvoice.setWaterPrice(water.getPrice());
        infoInvoice.setElectricPrice(electric.getPrice());
        infoInvoice.setRoomPrice(room.get().getPrice());
        infoInvoice.setService(service);
        return infoInvoice;
    }
}
