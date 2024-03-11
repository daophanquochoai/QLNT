package com.CNPM.QLNT.controller;

import com.CNPM.QLNT.exception.ResourceNotFoundException;
import com.CNPM.QLNT.model.bill;
import com.CNPM.QLNT.model.customer;
import com.CNPM.QLNT.model.donGia;
import com.CNPM.QLNT.model.room;
import com.CNPM.QLNT.response.BIllInRoom;
import com.CNPM.QLNT.response.Info_user;
import com.CNPM.QLNT.services.Inter.IBillService;
import com.CNPM.QLNT.services.Inter.ICustomerService;
import com.CNPM.QLNT.services.Impl.RoomService;
import com.CNPM.QLNT.services.Inter.IDonGiaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final RoomService roomService;
    private final ICustomerService iCustomerService;
    private final IDonGiaService iDonGiaService;
    private final IBillService iBillService;

    // 3. xem thong tin phong
    @GetMapping("/room/{room_id}")
    public ResponseEntity<Optional<room>> getRoom(@PathVariable int room_id){
        Optional<room> theRoom =roomService.getRoom(room_id);
        if( theRoom.isEmpty() ){
            throw new ResourceNotFoundException("Not Found Room");
        }
        return ResponseEntity.ok(Optional.of(theRoom.get()));
    }
    // 1/2. xem thong tin cua minh va ng khac
    @GetMapping("/customer/{cus_id}")
    public Optional<Info_user> getCustomerById(@PathVariable int cus_id){
        Optional<customer> theCustomer = iCustomerService.getCustomer(cus_id);
        if(theCustomer.isEmpty() ){
            throw new ResourceNotFoundException("Not Found Customer");
        }
        customer Customer = theCustomer.get();
        Info_user user = new Info_user(Customer.getCustomerId(),Customer.getFirstName(), Customer.getFirstName(), Customer.getCCCD(), Customer.getDate_of_birth(), Customer.getSex(), Customer.getInfoAddress(), Customer.getPhoneNumber(), Customer.getEmail(), Customer.getRoom().getId(), Customer.getUserAuthId().getUsersId().getUsername(), Customer.getUserAuthId().getUsersId().getPassword());
        return Optional.of(user);
    }

    // 4. xem gia dien, nuoc
    @GetMapping("/dongia/getAll")
    public List<donGia> getAll(){
        return iDonGiaService.getDonGia();
    }
    // 1. lay thong tin chu tro
    @GetMapping("/getAdmin")
    public Info_user getAdmin(){
        customer c = iCustomerService.getAdmin();
        Info_user admin = new Info_user(c.getCustomerId(),c.getFirstName(), c.getLastName(),c.getCCCD(),c.getDate_of_birth(),c.getSex(),c.getInfoAddress(), c.getPhoneNumber(),c.getEmail(),0,null,null);
        return admin;
    }
    // tra cuu hoa don moi nhat

}
