package com.CNPM.QLNT.controller;

import com.CNPM.QLNT.exception.ResourceNotFoundException;
import com.CNPM.QLNT.model.customer;
import com.CNPM.QLNT.services.ICustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final ICustomerService iCustomerService;
    @GetMapping("/getAllCustomer")
    public ResponseEntity<List<customer>> getAllCustomer(){
        List<customer> theCustomer = iCustomerService.getAllCustomer();
        return ResponseEntity.ok(theCustomer);
    }
    @GetMapping("/customer/{cus_id}")
    public Optional<customer> getCustomerById(@PathVariable int cus_id){
        Optional<customer> theCustomer = iCustomerService.getCustomer(cus_id);
        if(theCustomer.isEmpty() ){
            throw new ResourceNotFoundException("Not Found Customer");
        }
        return Optional.of(theCustomer.get());
    }
}
