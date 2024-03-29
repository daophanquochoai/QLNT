package com.CNPM.QLNT.controller;

import com.CNPM.QLNT.exception.ResourceNotFoundException;
import com.CNPM.QLNT.model.*;
import com.CNPM.QLNT.response.Info_user;
import com.CNPM.QLNT.response.Report;
import com.CNPM.QLNT.response.RoomRes;
import com.CNPM.QLNT.response.Statistical;
import com.CNPM.QLNT.services.Inter.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
@Slf4j
public class AdminController {
    private final ICustomerService iCustomerService;
    private final IContracService iContracService;
    private final IDonGiaService iDonGiaService;
    private final IRoomService iRoomService;
    private final IHomeCategory iHomeCategory;
    private final IBillService iBillService;
    private final ICommuService iCommuService;

    // lay tat ca phong
    @GetMapping("/getAllRoom")
    public ResponseEntity<List<RoomRes>> getAllRoom() throws SQLException {
        return ResponseEntity.ok(iRoomService.allRoom());
    }
    // 1. lay thong tin tat ca customer
    @GetMapping("/getAllCustomer")
    public ResponseEntity<List<Info_user>> getAllCustomer(){
        return ResponseEntity.ok(iCustomerService.getAllCustomer());
    }

    // 8. lay thong tin ng dung
    @GetMapping("/customer/{cus_id}")
    public ResponseEntity<?> getCustomerById(@PathVariable int cus_id){
       try{
           Optional<Customers> theCustomer = iCustomerService.getCustomer(cus_id);
           if(theCustomer.isEmpty() ){
               throw new ResourceNotFoundException("Khong tim  thay");
           }
           Customers Customers = theCustomer.get();
           int room;
           if(Customers.getRoom() == null) {
               room = 0;
           }else {
               room = Customers.getRoom().getId();
           }
           Info_user user = new Info_user(Customers.getCustomerId(), Customers.getFirstName(), Customers.getLastName(), Customers.getCCCD(), Customers.getDate_of_birth(), Customers.getSex(), Customers.getInfoAddress(), Customers.getPhoneNumber(), Customers.getEmail(), room, Customers.getUserAuthId().getUsersId().getUsername(), Customers.getUserAuthId().getUsersId().getPassword());
           return ResponseEntity.of(Optional.of(user));
       }
       catch (Exception ex){
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
       }
    }
    // 9. thay doi don gia
    @PostMapping("/dongia/add")
    @Transactional
    public ResponseEntity<?> saveDonGia(@RequestBody PriceQuotation dg){
        try{
            iDonGiaService.saveDonGia(dg);
            return ResponseEntity.ok("Chinh sua thanh cong");
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Loi he thong");
        }
    }
    // 13. lay don gia
    @GetMapping("/dongia/all")
    public ResponseEntity<List<?>> getAllDonGia(){
        return ResponseEntity.ok(iDonGiaService.getDonGia());
    }


    //1 .lay customer theo so phong
    @GetMapping("/customer/room/{roomd}")
    public ResponseEntity<?> getAllCustomeByRoomId(@PathVariable int roomd){
        try {
            return ResponseEntity.ok(iCustomerService.getCustomerByRoomId(roomd));
        }
        catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }
    // 3.them ng thue
    @PostMapping("/add/customer")
    public ResponseEntity<?> addCustomer(@RequestBody Info_user info){
        try{
            Boolean check = iCustomerService.addCustomer(info);
            return check ?  ResponseEntity.ok("Them thanh cong")
                    : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Phong day");
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }
    // 3.Sua nguoi thua
    @PutMapping("/update/customer/{id}")
    public ResponseEntity<?> updateCustomer(@PathVariable int id,
                                            @RequestBody Info_user info){
        try {
            iCustomerService.updateCustomer(id,info);
            return ResponseEntity.ok("Them thanh cong");
        }
        catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }
    // 3.Xoa khach thue
    @DeleteMapping("/remove/customer/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable int id){
        try {
            if(iCustomerService.getCustomer(id).get().getRoom() != null ){
                throw new ResourceNotFoundException("Khach hang da thue tro");
            }
            iContracService.getAllContract().stream().forEach(
                    c->{
                        if(c.getCusId().getCustomerId() == id && c.getEndDate().isAfter(LocalDate.now())){
                            throw new ResourceNotFoundException("Khach hang ki hop dong");
                        }
                    }
            );
            iCustomerService.deleteCustomer(id);
            return ResponseEntity.ok("Xoa thanh cong");
        }
        catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    //4.them phong///////////////////////////////////////////////////////////////////////////
    @GetMapping("/get/room/{idRoom}")
    public ResponseEntity<?> getRoom(@PathVariable int idRoom){
        Optional<Room> Room = iRoomService.getRoom(idRoom);
        if(Room.isPresent()){
            com.CNPM.QLNT.model.Room R = Room.get();
            return ResponseEntity.ok(new RoomRes(R.getId(),R.getLimit(),R.getHomeCategoryId().getHome_category_name(),R.getPrice(),R.getStatus()));
        }
        else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Khong tim thay phong");
        }
    }

    @PostMapping("/add/room")
    @Transactional
    public ResponseEntity<?> addRoom( @RequestBody RoomRes roomRes){
        try{
            iRoomService.addRoom(roomRes);
            return ResponseEntity.ok("Them thanh cong");
        }
        catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @PutMapping("update/room/{id}")
    @Transactional
    public ResponseEntity<?> updateRoom( @PathVariable int id, @RequestBody RoomRes roomRes){
        try{
            Optional<Room> Room = iRoomService.getRoom(id);
            if(Room.isPresent()){
                iRoomService.updateRoom(id,roomRes);
                return ResponseEntity.ok("Update thanh cong");
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Khong the tim thay phong do");
            }
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    @DeleteMapping("remove/room/{id}")
    @Transactional
    public ResponseEntity<?> deleteRoom( @PathVariable int id){
        try {
            iCustomerService.getAllCustomer().stream().forEach(
                    c-> {
                        if( c.getRoom() == id){
                            throw new ResourceNotFoundException("Co khach con o trong phong");
                        }
                    }
            );
            iContracService.getAllContract().stream().forEach(
                    c->{
                        if(c.getRoom().getId() == id && c.getEndDate().isAfter(LocalDate.now())){
                            throw new ResourceNotFoundException("Hop dong chua het han");
                        }
                    }
            );

            iRoomService.deleteRoom(id);
            return ResponseEntity.ok("Xoa thanh cong");
        }
        catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////

    //4. them home cate
    @PostMapping("/add/home_category")
    @Transactional
    public ResponseEntity<?> addHomeCate(@RequestBody HomeCategory homeCategory){
        try {
            iHomeCategory.addHomeCate(homeCategory);
            return ResponseEntity.ok("Them loai phong thanh cong");
        }
        catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Khong the them");
        }
    }

    //4. lay cac loai phong
    @GetMapping("/get/home_cate")
    public ResponseEntity<?> getAllHomeCate(){
        return ResponseEntity.ok(iHomeCategory.getAllHomeCate());
    }


    // 6. Loc theo trang thai
    @GetMapping("get/room/status/{status}")
    public ResponseEntity<?> getAllRoomByStaTus( @PathVariable boolean status){
        try{
            List<RoomRes> list = iRoomService.getAllRoomByStatus(status);
            return ResponseEntity.ok(list);
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    // 6. Lay theo phong trong
    @GetMapping("/get/room/limit/{type}")
    public ResponseEntity<?> getAllRoomByLimit( @PathVariable int type){
        try{
            return ResponseEntity.ok(iRoomService.getAllRoomByLimit(type));
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    //12 . Xem bao cao phong chua dong va so tien nhan duoc trong thang
    @GetMapping("get/report/{month}/{year}")
    public ResponseEntity<?> getReport(@PathVariable int month, @PathVariable int year){
        try {
            Report r = iBillService.getReport(month, year);
            return ResponseEntity.ok(r);
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    // lay yeu cau nguoi thue gui
    @GetMapping("get/notice/{status}")
    public ResponseEntity<?> getRequest(@PathVariable boolean status){
        try {
            return ResponseEntity.ok(iCommuService.getRequest(iCustomerService.getAdmin().getCustomerId(), status));
        }
        catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }
    // lay tat ca yeu cau
    @GetMapping("get/notice/all")
    public ResponseEntity<?> getAll(){
        try{
            return ResponseEntity.ok(iCommuService.getAll());
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }
    // update yeu cau
    @PutMapping("update/notice/{id}")
    public ResponseEntity<?> updateRequest(@PathVariable int id){
        try{
            iCommuService.updateRequest(id);
            return ResponseEntity.ok("Update Success");
        }catch (Exception ex)
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    // lay phong ton tai va co nguoi thue roi
    @GetMapping("get/room/bill")
    public ResponseEntity<?> getRoomForBill(){
        try{
            return ResponseEntity.ok(iRoomService.getRoomForBill());
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }
    // lay thong ke
    @GetMapping("/get/statistical")
    public ResponseEntity<?> getStatistical(){
        try{
            Statistical sta = new Statistical();
            sta.setDoanhThu(iBillService.getDoanhThu(LocalDate.now().getYear()));
            sta.setPhongDaDong(iBillService.getReport(LocalDate.now().getMonth().getValue()-1, LocalDate.now().getYear()).getDaDong().size());
            sta.setPhongChuaDong(iBillService.getReport(LocalDate.now().getMonth().getValue()-1, LocalDate.now().getYear()).getChuaDong().size());
            sta.setDaThueDay(iRoomService.getAllRoomByLimit(3).size());
            sta.setDaThueChuaDay(iRoomService.getAllRoomByLimit(2).size());
            sta.setChuaThue(iRoomService.getAllRoomByLimit(1).size());
            return ResponseEntity.ok(sta);
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }
    // xoa notice
    @DeleteMapping("remove/notice/{id}")
    public ResponseEntity<?> deletNoticeById(@PathVariable int id){
        try{
            iCommuService.deleteCommunication(id);
            return ResponseEntity.ok("Notice was deleted");
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

}
