package com.CNPM.QLNT.controller;

import com.CNPM.QLNT.exception.ResourceNotFoundException;
import com.CNPM.QLNT.model.*;
import com.CNPM.QLNT.response.Info_user;
import com.CNPM.QLNT.response.RoomRes;
import com.CNPM.QLNT.services.Inter.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.apache.coyote.http11.Constants.a;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final ICustomerService iCustomerService;
    private final IContracService iContracService;
    private final IDonGiaService iDonGiaService;
    private final IRoomService iRoomService;
    private final IHomeCategory iHomeCategory;
    // lay tat ca phong
    @GetMapping("/getAllRoom")
    public ResponseEntity<List<RoomRes>> getAllRoom() throws SQLException {
        return ResponseEntity.ok(iRoomService.allRoom());
    }
    // 1. lay thong tin customer
    @GetMapping("/getAllCustomer")
    public ResponseEntity<List<Info_user>> getAllCustomer(){
        return ResponseEntity.ok(iCustomerService.getAllCustomer());
    }

    // 1. lay thong tin ng dung
    @GetMapping("/customer/{cus_id}")
    public ResponseEntity<?> getCustomerById(@PathVariable int cus_id){
       try{
           Optional<customer> theCustomer = iCustomerService.getCustomer(cus_id);
           if(theCustomer.isEmpty() ){
               throw new ResourceNotFoundException("Khong tim  thay");
           }
           customer Customer = theCustomer.get();
           int room;
           if(Customer.getRoom() == null) {
               room = 0;
           }else {
               room = Customer.getRoom().getId();
           }
           Info_user user = new Info_user(Customer.getCustomerId(),Customer.getFirstName(), Customer.getLastName(), Customer.getCCCD(), Customer.getDate_of_birth(), Customer.getSex(), Customer.getInfoAddress(), Customer.getPhoneNumber(), Customer.getEmail(), room, Customer.getUserAuthId().getUsersId().getUsername(), Customer.getUserAuthId().getUsersId().getPassword());
           return ResponseEntity.of(Optional.of(user));
       }
       catch (Exception ex){
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
       }
    }
    // 9. thay doi don gia
    @PostMapping("/dongia/add")
    @Transactional
    public String saveDonGia(@RequestBody donGia dg){
        iDonGiaService.saveDonGia(dg);
        return "Chinh sua thanh cong";
    }
    // 13. lay don gia
    @GetMapping("/dongia/all")
    public List<donGia> getAllDonGia(){
        return iDonGiaService.getDonGia();
    }
    //1 .lay customer theo so phong
    @GetMapping("/customer/room/{roomd}")
    public ResponseEntity<?> getAllCustomeByRoomId(@PathVariable int roomd){
        try {
            return ResponseEntity.ok(iCustomerService.getCustomerByRoomId(roomd));
        }
        catch (Exception ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }
    // 3.them ng thue
    @Transactional
    @PostMapping("/add/customer")
    public ResponseEntity<?> addCustomer(@RequestBody Info_user info){
        try{
            Boolean check = iCustomerService.addCustomer(info);
            return check ?  ResponseEntity.ok("Them thanh cong")
                    : ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Phong day");
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }
    // 3.Sua nguoi thua
    @PutMapping("/update/customer/{id}")
    public ResponseEntity<?> updateCustomer(@PathVariable int id,
                                            @RequestBody Info_user info){
        try {
            iCustomerService.updateCustomer(id,info);
            return ResponseEntity.ok("Cap nhat thanh cong");
        }
        catch (Exception ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
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
                        if(c.getCusId().getCustomerId() == id && c.getEndDate().after(new Date())){
                            throw new ResourceNotFoundException("Khach hang ki hop dong");
                        }
                    }
            );
            iCustomerService.deleteCustomer(id);
            return ResponseEntity.ok("Xoa thanh cong");
        }
        catch (Exception ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    //4.them phong///////////////////////////////////////////////////////////////////////////
    @GetMapping("/get/room/{idRoom}")
    public ResponseEntity<?> getRoom(@PathVariable int idRoom){
        Optional<room> Room = iRoomService.getRoom(idRoom);
        if(Room.isPresent()){
            room R = Room.get();
            return ResponseEntity.ok(new RoomRes(R.getId(),R.getLimit(),R.getHomeCategoryId().getHome_category_name(),R.getPrice(),R.getStatus()));
        }
        else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Khong tim thay phong");
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
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @PutMapping("update/room/{id}")
    @Transactional
    public ResponseEntity<?> updateRoom( @PathVariable int id, @RequestBody RoomRes roomRes){
        try{
            Optional<room> Room = iRoomService.getRoom(id);
            if(Room.isPresent()){
                iRoomService.updateRoom(id,roomRes);
                return ResponseEntity.ok("Update thanh cong");
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Khong the tim thay phong do");
            }
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
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
                        if(c.getRoom().getId() == id && c.getEndDate().after(new Date())){
                            throw new ResourceNotFoundException("Hop dong chua het han");
                        }
                    }
            );

            iRoomService.deleteRoom(id);
            return ResponseEntity.ok("Xoa thanh cong");
        }
        catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////

    //4. them home cate
    @PostMapping("/add/home_category")
    @Transactional
    public ResponseEntity<?> addHomeCate(@RequestBody home_category homeCategory){
        try {
            iHomeCategory.addHomeCate(homeCategory);
            return ResponseEntity.ok("Them loai phong thanh cong");
        }
        catch (Exception ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Khong the them");
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
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

}
