package com.CNPM.QLNT.controller;

import com.CNPM.QLNT.exception.ResourceNotFoundException;
import com.CNPM.QLNT.model.*;
import com.CNPM.QLNT.response.*;
import com.CNPM.QLNT.services.Inter.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
@Slf4j
public class AdminController {
    private final ICustomerService iCustomerService;
    private final IContracService iContracService;
    private final IPriceService iPriceService;
    private final IRoomService iRoomService;
    private final IRoomTypeService iRoomTypeService;
    private final IBillService iBillService;
    private final IRequestService iRequestService;
    private final IHistoryCustomerService iHistoryCustomerService;
    private final IRoomService_Service iRoomServiceService;
    private final IService_Service iServiceService;

    // lay tat ca phong
    @GetMapping("/getAllRoom")
    public ResponseEntity<List<RoomRes>> getAllRoom() throws SQLException {
        return ResponseEntity.ok(iRoomService.getAllRoom());
    }

    // 1. lay thong tin customer
    @GetMapping("/getAllCustomer")
    public ResponseEntity<List<InfoUser>> getAllCustomer() {
        return ResponseEntity.ok(iCustomerService.getAllCustomer());
    }

    // 8. lay thong tin ng dung
    @GetMapping("/customer/{cus_id}")
    public ResponseEntity<?> getCustomerById(@PathVariable int cus_id) {
        try {
            Optional<Customer> theCustomer = iCustomerService.getCustomer(cus_id);
            if (theCustomer.isEmpty()) {
                throw new ResourceNotFoundException("Không tìm thấy khách thuê");
            }
            InfoUser user = new InfoUser(
                    theCustomer.get().getCustomerId(),
                    theCustomer.get().getFirstName(),
                    theCustomer.get().getLastName(),
                    theCustomer.get().getIdentifier(),
                    theCustomer.get().getDateOfBirth(),
                    theCustomer.get().getSex(),
                    theCustomer.get().getInfoAddress(),
                    theCustomer.get().getPhoneNumber(),
                    theCustomer.get().getEmail(),
                    theCustomer.get().getHistoryCustomer() == null ? null : theCustomer.get().getHistoryCustomer().stream().filter(t -> t.getEndDate() == null).findFirst().get().getRoomOld().getRoomId(),
                    theCustomer.get().getUserAuthId() == null ? "Chưa có tài khoản" : theCustomer.get().getUserAuthId().getUsername(),
                    theCustomer.get().getUserAuthId() == null ? "Chưa có tài khoản" : theCustomer.get().getUserAuthId().getPassword());
            return ResponseEntity.of(Optional.of(user));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    // 9. thay doi don gia
    @PostMapping("/price/electricPrice/add")
    @Transactional
    public ResponseEntity<?> savePriceQuotation(@RequestBody ElectricPrice e) {
        try {
            iPriceService.saveElecPrice(e);
            return ResponseEntity.ok("Thay đổi giá điện thành công");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi hệ thống");
        }
    }

    @PostMapping("/price/waterPrice/add")
    @Transactional
    public ResponseEntity<?> savePriceQuotation(@RequestBody WaterPrice w) {
        try {
            iPriceService.saveWaterPrice(w);
            return ResponseEntity.ok("Thay đổi giá nước thành công");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi hệ thống");
        }
    }

    // 13. lay don gia
    @GetMapping("/price/waterPrice/all")
    public ResponseEntity<List<?>> getAllWaterPrice() {
        return ResponseEntity.ok(iPriceService.getAllWaterPrice());
    }

    @GetMapping("/price/electricPrice/all")
    public ResponseEntity<List<?>> getAllElectricPrice() {
        return ResponseEntity.ok(iPriceService.getAllElectricPrice());
    }


    //1 .lay customer theo so phong
    @GetMapping("/get/customer/{roomId}")
    public ResponseEntity<?> getAllCustomerByRoomId(@PathVariable int roomId) {
        try {
            return ResponseEntity.ok(iCustomerService.getCustomerByRoomId(roomId));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    // 3.them ng thue
    @PostMapping("/add/customer")
    @Transactional
    public ResponseEntity<?> addCustomer(@RequestBody InfoUser info) {
        try {
            Boolean check = iCustomerService.addCustomer(info);
            return check ? ResponseEntity.ok("Thêm khách thuê thành công")
                    : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Phòng đã đầy");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    // 3.Sua nguoi thua
    @PutMapping("/update/customer/{id}")
    @Transactional
    public ResponseEntity<?> updateCustomer(@PathVariable int id,
                                            @RequestBody InfoUser info) {
        try {
            iCustomerService.updateCustomer(id, info);
            return ResponseEntity.ok("Thay đổi thông tin khách thuê thành công");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    // 3.Xoa khach thue
    @DeleteMapping("/remove/customer/{customerId}")
    public ResponseEntity<?> deleteCustomer(@PathVariable int customerId) {
        try {
            Customer customer = iCustomerService.getCustomer(customerId).get();
            Optional<HistoryCustomer> h = customer.getHistoryCustomer().stream().filter(t -> t.getEndDate() == null).findFirst();
            if (h.isPresent()) {
                InfoUser infoUser = new InfoUser();
                iCustomerService.updateCustomer(customerId,infoUser);
            } else iCustomerService.deleteCustomer(customerId);
            return ResponseEntity.ok("Xóa khách thuê thành công");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    //4.them phong///////////////////////////////////////////////////////////////////////////
    @GetMapping("/get/room/{roomId}")
    public ResponseEntity<?> getRoom(@PathVariable int roomId) {
        Optional<Room> Room = iRoomService.getRoomByRoomId(roomId);
        if (Room.isPresent()) {
            Room r = Room.get();
            return ResponseEntity.ok(new RoomRes(r.getRoomId(), r.getLimit(), r.getRoomType().getRoomTypeId(), r.getPrice()));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Không tìm thấy phòng");
        }
    }

    @PostMapping("/add/room")
    @Transactional
    public ResponseEntity<?> addRoom(@RequestBody RoomRes roomRes) {
        try {
            iRoomService.addRoom(roomRes);
            return ResponseEntity.ok("Thêm phòng thành công");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @PutMapping("update/room/{roomId}")
    @Transactional
    public ResponseEntity<?> updateRoom(@PathVariable int roomId, @RequestBody RoomRes roomRes) {
        try {
            Optional<Room> Room = iRoomService.getRoomByRoomId(roomId);
            if (Room.isPresent()) {
                iRoomService.updateRoom(roomId, roomRes);
                return ResponseEntity.ok("Thay đổi thông tin phòng thành công");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Không thể tìm thấy phòng");
            }
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @DeleteMapping("remove/room/{roomId}")
    @Transactional
    public ResponseEntity<?> deleteRoom(@PathVariable int roomId) {
        try {
            iCustomerService.getAllCustomer().stream().forEach(
                    c -> {
                        if (c.getRoomId() == roomId) {
                            throw new ResourceNotFoundException("Phòng đang có người ở");
                        }
                    }
            );
            iContracService.getAllContract().stream().forEach(
                    c -> {
                        if (c.getRoom().getRoomId() == roomId && c.getEndDate().isAfter(LocalDate.now())) {
                            throw new ResourceNotFoundException("Hợp đồng chưa hết hạn");
                        }
                    }
            );

            iRoomService.deleteRoom(roomId);
            return ResponseEntity.ok("Xóa phòng thành công");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }
    @DeleteMapping("remove/roomType/{roomTypeId}")
    @Transactional
    public ResponseEntity<?> deleteRoomType(@PathVariable int roomTypeId) {
        try {
            iRoomService.getAllRoom().stream().forEach(
                    r -> {
                        if (r.getRoomTypeId() == roomTypeId) {
                            throw new ResourceNotFoundException("Đang có phòng thuộc loại này");
                        }
                    }
            );
            iRoomTypeService.deleteRoomType(roomTypeId);
            return ResponseEntity.ok("Xóa loại phòng thành công");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////

    //4.them home cate
    @PostMapping("/add/roomType")
    @Transactional
    public ResponseEntity<?> addRoomType(@RequestBody RoomType roomType) {
        try {
            iRoomTypeService.addRoomType(roomType);
            return ResponseEntity.ok("Thêm loại phòng thành công");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    //4. lay cac loai phong
    @GetMapping("/get/roomType")
    public ResponseEntity<?> getAllRoomType() {
        return ResponseEntity.ok(iRoomTypeService.getAllRoomType());
    }

    // 6. Lay theo phong trong
    @GetMapping("/get/room/limit/{type}")
    public ResponseEntity<?> getAllRoomByLimit(@PathVariable int type) {
        try {
            return ResponseEntity.ok(iRoomService.getAllRoomByLimit(type));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @GetMapping("/getAllRoomWithContract")
    public ResponseEntity<?> getAllRoomWithContract() {
        try {
            return ResponseEntity.ok(iRoomService.getAllRoomWithContract());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    //12 . Xem bao cao phong chua dong va so tien nhan duoc trong thang
    @GetMapping("get/report/{month}/{year}")
    public ResponseEntity<?> getReport(@PathVariable int month, @PathVariable int year) {
        try {
            Report r = iBillService.getReport(month, year);
            return ResponseEntity.ok(r);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    // lay yeu cau nguoi thue gui
    @GetMapping("get/notice/{status}")
    public ResponseEntity<?> getRequest(@PathVariable boolean status) {
        try {
            return ResponseEntity.ok(iRequestService.getAllRequestOfCustomerByStatus(status));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    // lay tat ca yeu cau
    @GetMapping("get/notice/all")
    public ResponseEntity<?> getAll() {
        try {
            return ResponseEntity.ok(iRequestService.getAllRequestOfCustomer());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    // update yeu cau
    @PutMapping("update/notice/{id}")
    public ResponseEntity<?> updateRequest(@PathVariable int id) {
        try {
            iRequestService.updateRequest(id);
            return ResponseEntity.ok("Đã xử lý yêu cầu");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    // lay phong ton tai va co nguoi thue roi
    @GetMapping("get/room/bill")
    public ResponseEntity<?> getRoomForBill() {
        try {
            return ResponseEntity.ok(iRoomService.getRoomForBill());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    // lay thong ke
    @GetMapping("/get/statistical")
    public ResponseEntity<?> getStatistical() {
        try {
            Statistical sta = new Statistical();
            sta.setRevenue(iBillService.getRevenue(LocalDate.now().getYear()));
            sta.setNumberOfPaidRoom(iBillService.getReport(LocalDate.now().getMonth().getValue() - 1, LocalDate.now().getYear()).getPaidRoomList().size());
            sta.setNumberOfUnpaidRoom(iBillService.getReport(LocalDate.now().getMonth().getValue() - 1, LocalDate.now().getYear()).getUnpaidRoomList().size());
            sta.setNumberOfFullRoom(iRoomService.getAllRoomByLimit(3).size());
            sta.setNumberOfAvailableRoom(iRoomService.getAllRoomByLimit(2).size());
            sta.setNumberOfEmptyRoom(iRoomService.getAllRoomByLimit(1).size());
            return ResponseEntity.ok(sta);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    // xoa notice
    @DeleteMapping("remove/notice/{id}")
    @Transactional
    public ResponseEntity<?> deleteNoticeById(@PathVariable int id) {
        try {
            iRequestService.deleteCommunication(id);
            return ResponseEntity.ok("Yêu cầu đã được xóa");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    // lay lich su chuyen di
    @GetMapping("get/history/all")
    public ResponseEntity<?> getAllHistory() {
        try {
            return ResponseEntity.ok(iHistoryCustomerService.getAllHistoryCustomer());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    //     tinh hoa don
    @PostMapping("add/bill")
    @Transactional
    public ResponseEntity<?> billCalculation(@RequestBody BillInRoom billInRoom) {
        try {
            iBillService.addBill(billInRoom);
            return ResponseEntity.ok("Thêm hóa đơn thành công");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @GetMapping("/get/bill/{roomId}/{month}/{year}")
    public ResponseEntity<?> getBillByRoom(
            @PathVariable Integer roomId,
            @PathVariable Integer month,
            @PathVariable Integer year
    ){
        try{
            if( month > 12 || month <= 0) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("month");
            if( year > LocalDate.now().getYear() || year < 0 ) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("year");
            return ResponseEntity.ok(iBillService.getBillByRoomInMonthInYear(roomId,month,year));
        }catch ( Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    // Lấy thông tin phòng để tính hóa đơn
    @GetMapping("get/invoiceInfo/{roomId}/{month}/{year}")
    public ResponseEntity<?> getServiceByRoomIdAndDate(@PathVariable Integer roomId,
                                                       @PathVariable Integer month,
                                                       @PathVariable Integer year) {
        try {
            return ResponseEntity.ok(iBillService.getInfoToAddInvoice(roomId, month,year));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @GetMapping("get/service/all")
    public ResponseEntity<?> getAllService() {
        try {
            return ResponseEntity.ok(iServiceService.getAllService());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @PostMapping("add/service")
    @Transactional
    public ResponseEntity<?> saveService(@RequestBody Service service) {
        try {
            iServiceService.saveService(service);
            return ResponseEntity.ok("Thêm dịch vụ thành công");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @PutMapping("update/service/{id}")
    @Transactional
    public ResponseEntity<?> updateService(@PathVariable Integer id, @RequestBody Service service) {
        try {
            iServiceService.updateService(id, service);
            return ResponseEntity.ok("Cập nhật dịch vụ thành công");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @PostMapping("add/roomService/{id}")
    @Transactional
    public ResponseEntity<?> saveRoomService(@PathVariable Integer id,
                                             @RequestBody InfoRoomService infoRoomService) {
        try {
            iRoomServiceService.saveRoomService(id, infoRoomService);
            return ResponseEntity.ok("Thêm dịch vụ thành công");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @GetMapping("/get/roomService/{roomId}")
    public ResponseEntity<?> getServiceByRoom(
            @PathVariable Integer roomId
    ){
        try{;
            return ResponseEntity.ok(iRoomServiceService.getServiceByRoomIdMonthYear(roomId));
        }catch ( Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    //////// xem hop dong
    @GetMapping("getAllContract")
    public ResponseEntity<?> getAllContract() {
        try {
            return ResponseEntity.ok(iContracService.getAllContract());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @PostMapping("add/contract/{customerId}/{roomId}")
    public ResponseEntity<?> saveContract(@PathVariable Integer customerId,
                                          @PathVariable Integer roomId,
                                          @RequestBody InfoContract infoContract) {
        try {
            iContracService.saveContract(customerId, roomId, infoContract);
            return ResponseEntity.ok("Lưu hợp đồng thành công");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }
}
