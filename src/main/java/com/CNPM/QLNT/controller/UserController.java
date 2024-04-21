package com.CNPM.QLNT.controller;

import com.CNPM.QLNT.exception.ResourceNotFoundException;
import com.CNPM.QLNT.model.*;
import com.CNPM.QLNT.response.InfoContract;
import com.CNPM.QLNT.response.InfoUser;
import com.CNPM.QLNT.services.Inter.*;
import com.CNPM.QLNT.services.Impl.RoomService;
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
    private final IPriceService iPriceService;
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
    public ResponseEntity<?> getCustomerById(@PathVariable Integer cus_id){
       try{
           Optional<Customer> theCustomer = iCustomerService.getCustomer(cus_id);
           if(theCustomer.isEmpty() ){
               throw new ResourceNotFoundException("Not Found Customer");
           }
           Customer Customer = theCustomer.get();
//           System.out.println("===========================");
//           log.info("{}",Customer.getHistoryCustomer().stream().filter(t->t.getEndDate()==null).findFirst().get().getRoomOld().getId());
//           System.out.println("===========================");
        InfoUser user = new InfoUser(
                Customer.getCustomerId(),
                Customer.getFirstName(),
                Customer.getFirstName(),
                Customer.getIdentifier(),
                Customer.getDateOfBirth(),
                Customer.getSex(),
                Customer.getInfoAddress(),
                Customer.getPhoneNumber(),
                Customer.getEmail(),
                Customer.getHistoryCustomer().isEmpty() ? -1 : Customer.getHistoryCustomer().stream().filter(t->t.getEndDate()==null).findFirst().get().getRoomOld().getRoomId(),
                Customer.getUserAuthId() == null ? "Chưa có tài khoản" : Customer.getUserAuthId().getUsername(),
                Customer.getUserAuthId() == null ? "Chưa có tài khoản" : Customer.getUserAuthId().getPassword());
           return ResponseEntity.ok(user);
       }catch (Exception ex){
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
       }
    }

    // 4. xem gia dien, nuoc
    @GetMapping("/gianuoc/all")
    public ResponseEntity<List<?>> getAllWaterPrice(){
        return ResponseEntity.ok(iPriceService.getAllWaterPrice());
    }
    @GetMapping("/giadien/all")
    public ResponseEntity<List<?>> getAllElecPrice(){
        return ResponseEntity.ok(iPriceService.getAllElectricPrice());
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
            Optional<Customer> customers = iCustomerService.getCustomer(id);
            if( customers.isEmpty()) return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Not found Customer");
            Request request = new Request();
            request.setCustomer(customers.get());
            request.setCreatedDate(LocalDateTime.now());
            request.setStatus(false);
            request.setMessage(mess);
            request.setIsSend(true);
            iRequestService.addRequest_DonGia(request);
            return ResponseEntity.ok("Them thanh cong");
        }catch (Exception ex){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    //9. Xem hop dong cua minh
    @GetMapping("get/contract/{id}")
    public ResponseEntity<?> getContract(@PathVariable Integer id){
        try{
            Contract c = iContracService.getContractById(id);
            InfoContract ic = new InfoContract();
            ic.setBeginDate(c.getBeginDate());
            ic.setCreatedDate(c.getCreatedDate());
            ic.setEndDate(c.getEndDate());
            ic.setStatus(c.getStatus());
            ic.setRoomId(c.getRoom().getRoomId());
            ic.setCustomerId(c.getCustomer().getCustomerId());
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
