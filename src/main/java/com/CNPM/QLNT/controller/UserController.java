package com.CNPM.QLNT.controller;

import com.CNPM.QLNT.exception.ResourceNotFoundException;
import com.CNPM.QLNT.model.*;
import com.CNPM.QLNT.response.InfoContract;
import com.CNPM.QLNT.response.Info_user;
import com.CNPM.QLNT.services.Inter.*;
import com.CNPM.QLNT.services.Impl.RoomService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    private final IRequestService  iRequestService;
    private final IContracService iContracService;
    private final IHistoryCustomerService iHistoryCustomerService;

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
    public Optional<?> getCustomerById(@PathVariable Integer cus_id){
        Optional<Customers> theCustomer = iCustomerService.getCustomer(cus_id);
        if(theCustomer.isEmpty() ){
            throw new ResourceNotFoundException("Not Found Customer");
        }
        Customers Customer = theCustomer.get();
        Info_user user = new Info_user(
                Customer.getCustomerId(),
                Customer.getFirstName(),
                Customer.getFirstName(),
                Customer.getCCCD(),
                Customer.getDate_of_birth(),
                Customer.getSex(),
                Customer.getInfoAddress(),
                Customer.getPhoneNumber(),
                Customer.getEmail(),
                Customer.getHistoryCustomer().size() == 0 ? -1 : Customer.getHistoryCustomer().stream().filter(t->t.getEndDate()==null).findFirst().get().getRoomOld().getId(),
                Customer.getUserAuthId() == null ? "Chưa có tài khoản" : Customer.getUserAuthId().getUsername(),
                Customer.getUserAuthId() == null ? "Chưa có tài khoản" : Customer.getUserAuthId().getPassword());
        return Optional.of(user);
    }

    // 4. xem gia dien, nuoc
    @GetMapping("/gianuoc/all")
    public ResponseEntity<List<?>> getAllWaterPrice(){
        return ResponseEntity.ok(iDonGiaService.getAllWaterPrice());
    }
    @GetMapping("/giadien/all")
    public ResponseEntity<List<?>> getAllElecPrice(){
        return ResponseEntity.ok(iDonGiaService.getAllElectricProce());
    }
    // 1. lay thong tin chu tro
//    @GetMapping("/getAdmin")
//    public Info_user getAdmin(){
//        Customers c = iCustomerService.getAdmin();
//        Info_user admin = new Info_user(c.getCustomerId(),c.getFirstName(), c.getLastName(),c.getCCCD(),c.getDate_of_birth(),c.getSex(),c.getInfoAddress(), c.getPhoneNumber(),c.getEmail(),0,null,null);
//        return admin;
//    }
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
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    //7 . Nhan thong bao tu chu tro
    @GetMapping("get/notice/{id}")
    public ResponseEntity<?> getNoticce(@PathVariable Integer id){
        try {
            return ResponseEntity.ok(iRequestService.getNoticeBySender(id));
        }
        catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }

    // 8. Gui yeu cau den chu tro
    @PostMapping("add/notice/{id}")
//    @Transactional
    public ResponseEntity<?> addNotice(@PathVariable int id, @RequestBody String mess){
        try{
            Requests request = new Requests();
            request.setCreatedDatatime(LocalDateTime.now());
            request.setStatus(false);
            request.setMessage(mess);
            request.setSenOrRei(true);
            iRequestService.addRequest_DonGia(request);
            return ResponseEntity.ok("Them thanh cong");
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    //9. Xem hopv dong cua  minh
    @GetMapping("get/contract/{id}")
    public ResponseEntity<?> getContract(@PathVariable Integer id){
        try{
            Contracts c = iContracService.getContractById(id);
            InfoContract ic = new InfoContract();
            ic.setBeginDate(c.getBeginDate());
            ic.setConDate(c.getConDate());
            ic.setEndDate(c.getEndDate());
            ic.setStatus(c.getStatus());
            ic.setRoomId(c.getRoom().getId());
            ic.setCustomerId(c.getCusId().getCustomerId());
            return ResponseEntity.ok(ic);
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    // xem thong nguoi cua tat cả nguoi chung phong
    @GetMapping("get/customer/room/{roomId}")
    public ResponseEntity<?> getAllCustomerByRoomId(@PathVariable Integer roomId){
        try {
            return ResponseEntity.ok(iHistoryCustomerService.getAllCustomerByRoom(roomId));
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Loi");
        }
    }

}
