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

    @GetMapping("/getAllRoom")
    public ResponseEntity<List<room>> getAllRoom() throws SQLException{
        return ResponseEntity.ok(roomService.allRoom());
    }

    @GetMapping("/room/{room_id}")
    public ResponseEntity<Optional<room>> getRoom(@PathVariable int room_id){
        Optional<room> theRoom =roomService.getRoom(room_id);
        if( theRoom.isEmpty() ){
            throw new ResourceNotFoundException("Not Found Room");
        }
        return ResponseEntity.ok(Optional.of(theRoom.get()));
    }

    @GetMapping("/customer/{cus_id}")
    public Optional<Info_user> getCustomerById(@PathVariable int cus_id){
        Optional<customer> theCustomer = iCustomerService.getCustomer(cus_id);
        if(theCustomer.isEmpty() ){
            throw new ResourceNotFoundException("Not Found Customer");
        }
        customer Customer = theCustomer.get();
        Info_user user = new Info_user(Customer.getFirst_name(), Customer.getLast_name(), Customer.getCCCD(), Customer.getDate_of_birth(), Customer.getSex(), Customer.getInfo_address(), Customer.getPhone_number(), Customer.getEmail(), Customer.getRoom().getRoom_id(), Customer.getUA().getUsers_id().getUsername(), Customer.getUA().getUsers_id().getPassword());
        return Optional.of(user);
    }

    //xem gia dien, nuoc
    @GetMapping("/dongia/getAll")
    public List<donGia> getAll(){
        return iDonGiaService.getDonGia();
    }

    @GetMapping("/getAdmin")
    public Info_user getAdmin(){
        customer c = iCustomerService.getAdmin();
        Info_user admin = new Info_user(c.getFirst_name(), c.getLast_name(),c.getCCCD(),c.getDate_of_birth(),c.getSex(),c.getInfo_address(), c.getPhone_number(),c.getEmail(),0,null,null);
        return admin;
    }

    @GetMapping("/bill/{room_id}")
    public BIllInRoom getBillInRoom(@PathVariable int room_id){
        Optional<bill> b = iBillService.getBill(room_id);
        if( b.isEmpty() ) throw new ResourceNotFoundException("Not found bill");
        bill b1 = b.get();
        BIllInRoom Br = new BIllInRoom();
        Br.setNumberBill(b1.getBill_id());
        Br.setNumberRoom(b1.getRoom().getRoom_id());
        Br.setDay_Begin(b1.getBegin_date());
        Br.setDay_End(b1.getEnd_date());
        Br.setNumber_E_Begin(b1.getElectric_number_begin());
        Br.setNumber_E_End(b1.getElectric_number_end());
        Br.setNumber_W_Begin(b1.getWater_number_begin());
        Br.setNumber_W_End(b1.getWater_number_end());
        Br.setOther_Price(b1.getOther_price());
        Br.setGhi_Chu(b1.getGhiChu());
        donGia dg = iDonGiaService.getDonGiaNow();
        BigInteger numberE = BigInteger.valueOf(Br.getNumber_E_End() - Br.getNumber_E_Begin());
        BigInteger numberW = BigInteger.valueOf(Br.getNumber_W_End() - Br.getNumber_W_Begin());
        BigInteger giaDien = numberE.multiply(BigInteger.valueOf(dg.getGiaDien()));
        BigInteger giaNuoc = numberW.multiply(BigInteger.valueOf(dg.getGiaNuoc()));
        BigInteger result = giaDien.add(giaNuoc).add(BigInteger.valueOf(Br.getOther_Price()));
        Br.setThanh_Tien(result);
        return Br;
    }
}
