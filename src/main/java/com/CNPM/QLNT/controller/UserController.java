package com.CNPM.QLNT.controller;

import com.CNPM.QLNT.exception.ResourceNotFoundException;
import com.CNPM.QLNT.model.Customers;
import com.CNPM.QLNT.model.PriceQuotation;
import com.CNPM.QLNT.model.Requests;
import com.CNPM.QLNT.model.Room;
import com.CNPM.QLNT.response.Info_user;
import com.CNPM.QLNT.services.Inter.*;
import com.CNPM.QLNT.services.Impl.RoomService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final RoomService roomService;
    private final ICustomerService iCustomerService;
    private final IDonGiaService iDonGiaService;
    private final IBillService iBillService;
    private final ICommuService  iCommuService;
    private final IContracService iContracService;

    // 3. xem thong tin phong
    @GetMapping("/room/{room_id}")
    public ResponseEntity<Optional<Room>> getRoom(@PathVariable int room_id){
        Optional<Room> theRoom =roomService.getRoom(room_id);
        if( theRoom.isEmpty() ){
            throw new ResourceNotFoundException("Not Found Room");
        }
        return ResponseEntity.ok(Optional.of(theRoom.get()));
    }
    // 1/2. xem thong tin cua minh va ng khac
    @GetMapping("/customer/{cus_id}")
    public Optional<Info_user> getCustomerById(@PathVariable int cus_id){
        Optional<Customers> theCustomer = iCustomerService.getCustomer(cus_id);
        if(theCustomer.isEmpty() ){
            throw new ResourceNotFoundException("Not Found Customer");
        }
        Customers Customer = theCustomer.get();
        Info_user user = new Info_user(Customer.getCustomerId(),Customer.getFirstName(), Customer.getFirstName(), Customer.getCCCD(), Customer.getDate_of_birth(), Customer.getSex(), Customer.getInfoAddress(), Customer.getPhoneNumber(), Customer.getEmail(), Customer.getRoom().getId(), Customer.getUserAuthId().getUsersId().getUsername(), Customer.getUserAuthId().getUsersId().getPassword());
        return Optional.of(user);
    }

    // 4. xem gia dien, nuoc
    @GetMapping("/dongia/getAll")
    public List<PriceQuotation> getAll(){
        return iDonGiaService.getDonGia();
    }
    // 1. lay thong tin chu tro
    @GetMapping("/getAdmin")
    public Info_user getAdmin(){
        Customers c = iCustomerService.getAdmin();
        Info_user admin = new Info_user(c.getCustomerId(),c.getFirstName(), c.getLastName(),c.getCCCD(),c.getDate_of_birth(),c.getSex(),c.getInfoAddress(), c.getPhoneNumber(),c.getEmail(),0,null,null);
        return admin;
    }
    // 5. Xem hoa don cua phong minh
    @GetMapping("get/bill/{room}")
    public ResponseEntity<?>  getAllBillByRoom(@PathVariable int room){
        try {
            return ResponseEntity.ok(iBillService.getAllBillByRoom(room));
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    //6 Tra cuu hoa don dong chua
    @GetMapping("get/bill/{room}/{status}")
    public ResponseEntity<?>  getAllBillByStatus(@PathVariable int room, @PathVariable boolean status){
        try {
            return ResponseEntity.ok(iBillService.getAllBillByStatus(room,status));
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    //7 . Nhan thong bao tu chu tro
    @GetMapping("get/notice/{id}")
    public ResponseEntity<?> getNoticce(@PathVariable int id){
        try {
            return ResponseEntity.ok(iCommuService.getNoticeBySender(id));
        }
        catch (Exception ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    // 8. Gui yeu cau den chu tro
    @PostMapping("add/notice/{id}")
    @Transactional
    public ResponseEntity<?> addNotice(@PathVariable int id, @RequestBody String mess){
        try{
            Requests request = new Requests();
            request.setCreatedDatatime(LocalDateTime.now());
            request.setStatus(true);
            request.setMessage(mess);
            iCommuService.addMessage(request,  iCustomerService.getCustomer(id).get(),iCustomerService.getAdmin());
            return ResponseEntity.ok("Them thanh cong");
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    //9. Xem hopv dong cua  minh
    @GetMapping("get/contract/{id}")
    public ResponseEntity<?> getContract(@PathVariable int id){
        try{
            return ResponseEntity.ok(iContracService.getContractById(id));
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

}
