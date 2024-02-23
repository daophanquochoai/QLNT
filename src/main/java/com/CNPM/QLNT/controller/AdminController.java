package com.CNPM.QLNT.controller;

import com.CNPM.QLNT.exception.ResourceNotFoundException;
import com.CNPM.QLNT.model.customer;
import com.CNPM.QLNT.model.donGia;
import com.CNPM.QLNT.model.requests;
import com.CNPM.QLNT.response.Info_user;
import com.CNPM.QLNT.services.Inter.ICommuService;
import com.CNPM.QLNT.services.Inter.ICustomerService;
import com.CNPM.QLNT.services.Inter.IDonGiaService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final ICustomerService iCustomerService;
    private final IDonGiaService iDonGiaService;
    private final ICommuService iCommuService;
    @GetMapping("/getAllCustomer")
    public ResponseEntity<List<customer>> getAllCustomer(){
        List<customer> theCustomer = iCustomerService.getAllCustomer();
        return ResponseEntity.ok(theCustomer);
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
    @PostMapping("/dongia/add")
    @Transactional
    public String saveDonGia(@RequestBody donGia dg){
        iDonGiaService.saveDonGia(dg);
        String mess = "Thay doi : Tien dien = "+ dg.getGiaDien() +" , Tien nuoc = "+ dg.getGiaNuoc() +" , Thoi gian thay doi = "+ dg.getTimeChange();
        requests request = new requests();
        request.setCreated_datatime(new Date());
        request.setStatus(true);
        request.setMessage(mess);
        customer chutro = iCustomerService.getAdmin();
        iCommuService.addMessage(request,chutro,null);
        return "Chinh sua thanh cong";
    }

}
