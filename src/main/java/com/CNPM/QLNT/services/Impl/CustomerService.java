package com.CNPM.QLNT.services.Impl;

import com.CNPM.QLNT.exception.ResourceNotFoundException;
import com.CNPM.QLNT.model.*;
import com.CNPM.QLNT.repository.CustomerRepository;
import com.CNPM.QLNT.repository.HistoryCustomerRepo;
import com.CNPM.QLNT.repository.RoomRepo;
import com.CNPM.QLNT.response.InfoLogin;
import com.CNPM.QLNT.response.Info_user;
import com.CNPM.QLNT.services.Inter.IContracService;
import com.CNPM.QLNT.services.Inter.ICustomerService;
import com.CNPM.QLNT.services.Inter.IRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService implements ICustomerService {
    private final CustomerRepository customerRepository;
    private final IRoomService iRoomService;
    private final IContracService iContracService;
    private final HistoryCustomerRepo historyCustomerRepo;
    private final RoomRepo roomRepo;
    private final String Email_Regex = "^\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";

    private final Pattern pattern = Pattern.compile(Email_Regex);
    @Override
    public List<Info_user> getAllCustomer() {

        List<Customers> list = customerRepository.findAll();
        List<Info_user> l = list.stream()
                .filter(c-> c.getUserAuthId().getRole().equals("USER"))
                .map(
                c ->
                {
                    Integer roomId = -1;
                    if( c.getHistoryCustomer() != null ){
                        Optional<HistoryCustomer> h = c.getHistoryCustomer().stream().filter( t-> (t.getEndDate() == null && t.getCustomerId().getCustomerId() == c.getCustomerId())).findFirst();
                        if( h.isPresent()) roomId = h.get().getRoomOld().getRoomId();
                    }
                    Info_user user = new Info_user(c.getCustomerId(),
                    c.getFirstName(),
                    c.getLastName(),
                    c.getIdentifier(),
                    c.getDate_of_birth(),
                    c.getSex(),
                    c.getInfoAddress(),
                    c.getPhoneNumber(),
                    c.getEmail(),
                    roomId,
                     c.getUserAuthId() == null ? "Chưa có tài khoản" : c.getUserAuthId().getUsername(),
                    c.getUserAuthId() == null ? "Chưa có tài khoản" : c.getUserAuthId().getPassword()
                    );
                    return user;
                }
        ).collect(Collectors.toList());
        return l;
    }

    @Override
    public Optional<Customers> getCustomer(int cus_id) {
        return Optional.of(customerRepository.findById(cus_id).get());
    }

    @Override
    public List<Info_user> getCustomerByRoomId(Integer room_id) {
        List<Customers> list = historyCustomerRepo.getCustmersByRoom(room_id);
        List<Info_user> l = list.stream().map(
                c->
                {
                    Info_user user = new Info_user(
                            c.getCustomerId(),
                            c.getFirstName(),
                            c.getLastName(),
                            c.getIdentifier(),
                            c.getDate_of_birth(),
                            c.getSex(),
                            c.getInfoAddress(),
                            c.getPhoneNumber(),
                            c.getEmail(),
                            room_id,
                            c.getUserAuthId() == null ? "Chưa có tài khoản" : c.getUserAuthId().getUsername(),
                            c.getUserAuthId() == null ? "Chưa có tài khoản" : c.getUserAuthId().getPassword()
                    );
                    return user;
                }
        ).collect(Collectors.toList());
        return l;
    }

    @Override
    public Boolean addCustomer(Info_user info) throws Exception {
        List<Info_user> listCus = getAllCustomer();
       Customers c = new Customers();
       if( !listCus.isEmpty()){
           boolean check = getAllCustomer().stream().anyMatch(
                   cus -> ( cus.getCCCD().equals(info.getCCCD())|| cus.getTaikhoan().equals(info.getTaikhoan()))
           );
           if( check) throw new ResourceNotFoundException("Bi trung Identify hoac Tai khoan");
       }
        List<Info_user> list = getCustomerByRoomId(info.getRoom());
        if( info.getRoom() != 0 && !iRoomService.getAllRoomByStatus(true).isEmpty()){
            Room Room = iRoomService.getRoom(info.getRoom()).get();
            if( Room.getLimit() == list.size()){
                return false;
            }
        }

        c.setFirstName(info.getFirst_name());
        c.setLastName(info.getLast_name());
        if( info.getCCCD() == null || info.getCCCD().length() != 12){
            throw new ResourceNotFoundException("Identify");
        }
        c.setIdentifier(info.getCCCD());
        if(info.getDate_of_birth() != null){
            if( info.getDate_of_birth().isAfter(LocalDate.now()) ) throw new ResourceNotFoundException("dateOfBirth");
            c.setDate_of_birth(info.getDate_of_birth());
        }
        if(info.getSex() != null){
            c.setSex(info.getSex());
        }
        if(info.getInfo_address() != null){
            c.setInfoAddress(info.getInfo_address());
        }
        if(info.getPhone_number() != null){
            if( info.getPhone_number().length() != 10 || info.getPhone_number().charAt(0) != '0')  throw new ResourceNotFoundException("phoneNumber");
            c.setPhoneNumber(info.getPhone_number());
        }
        if(info.getEmail() != null){
            Matcher matcher = pattern.matcher(info.getEmail());
            if( !matcher.matches()) throw new ResourceNotFoundException("email");
            c.setEmail(info.getEmail());
        }
        HistoryCustomer historyCustomer = new HistoryCustomer();
        if( info.getRoom() != 0 ){
            if( roomRepo.findById(info.getRoom()).isEmpty()) throw new ResourceNotFoundException("room");
            else{
                Room r = roomRepo.findById(info.getRoom()).get();
                if( r.getLimit() < historyCustomerRepo.getCustmersByRoom(r.getRoomId()).size() ) throw new ResourceNotFoundException("room day");
                historyCustomer.setRoomOld(r);
                historyCustomer.setBeginDate(LocalDate.now());
            }
        }

        UserAuth ua = new UserAuth();
        ua.setUsername(info.getTaikhoan());
        ua.setPassword(new BCryptPasswordEncoder().encode(info.getMatkhau()));
        ua.setActive(true);
        ua.setRole("USER");
        c.setUserAuthId(ua);
        Customers customers = customerRepository.save(c);
        if( info.getRoom() != 0 ){
            historyCustomer.setCustomerId(customers);
            historyCustomerRepo.save(historyCustomer);
        }
        return true;
    }

    @Override
    public void updateCustomer(int id, Info_user info) {
        Optional<Customers> C = getCustomer(id);
        if( C.isEmpty()) throw new ResourceNotFoundException("Khong tim thay customer");
        Customers Customer = C.get();
        boolean check = getAllCustomer().stream().anyMatch(
                c -> (c.getId() != id && ( c.getCCCD().equals(info.getCCCD())|| c.getTaikhoan().equals(info.getTaikhoan())))
        );
        if( check) throw new ResourceNotFoundException("Bi trung CCCD hoac TK_MK");

        if( info.getCCCD() == null || info.getCCCD().length() != 12){
            throw new ResourceNotFoundException("Identify");
        }

        if( info.getFirst_name() !=  null){
            Customer.setFirstName(info.getFirst_name());
        }
        if( info.getLast_name() !=  null){
            Customer.setLastName(info.getLast_name());
        }
        if( info.getCCCD() !=  null){
            Customer.setIdentifier(info.getCCCD());
        }
        if( info.getInfo_address() !=  null){
            Customer.setInfoAddress(info.getInfo_address());
        }
        if( info.getDate_of_birth() !=  null){
            if( info.getDate_of_birth().isAfter(LocalDate.now()) ) throw new ResourceNotFoundException("dateOfBirth");
            Customer.setDate_of_birth(info.getDate_of_birth());
        }
        if( info.getSex() !=  null){
            Customer.setSex(info.getSex());
        }
        if( info.getPhone_number() !=  null){
            if( info.getPhone_number().length() != 10 || info.getPhone_number().charAt(0) != '0')  throw new ResourceNotFoundException("phoneNumber");
            Customer.setPhoneNumber(info.getPhone_number());
        }
        if( info.getEmail() !=  null){
            Matcher matcher = pattern.matcher(info.getEmail());
            if( !matcher.matches()) throw new ResourceNotFoundException("email");
            Customer.setEmail(info.getEmail());
        }
        if( info.getTaikhoan() !=  null){
            Customer.getUserAuthId().setUsername(info.getTaikhoan());
        }
        if( info.getMatkhau() !=  null) {
            Customer.getUserAuthId().setPassword(new BCryptPasswordEncoder().encode(info.getMatkhau()));
        }
        if( info.getRoom() != 0 ) {
            Optional<HistoryCustomer> h = Customer.getHistoryCustomer().stream().filter(t-> t.getEndDate() == null).findFirst();
            if( roomRepo.findById(info.getRoom()).isEmpty()) throw new ResourceNotFoundException("Khong tim thay phong");
            if( h.isPresent()){
                h.get().setEndDate(LocalDate.now());
                h.get().setRoomNew(   roomRepo.findById(info.getRoom()).get());
            }
            HistoryCustomer historyCustomer = new HistoryCustomer();
            historyCustomer.setBeginDate(LocalDate.now());
            historyCustomer.setRoomOld(roomRepo.findById(info.getRoom()).get());
            historyCustomer.setCustomerId(Customer);
            historyCustomerRepo.save(historyCustomer);
        }else{
            Optional<HistoryCustomer> h = Customer.getHistoryCustomer().stream().filter(t-> t.getEndDate() == null).findFirst();
            if( h.isPresent()){
                h.get().setEndDate(LocalDate.now());
            }
        }
        customerRepository.save(Customer);
    }

    @Override
    public void deleteCustomer(int id) {
        try{
            List<Contracts> listCT = iContracService.getAllContract();
            listCT.stream().forEach( c ->{
                if( c.getCusId().getCustomerId() == id && (c.getEndDate().isAfter(LocalDate.now()) || !c.getStatus()) ){
                    System.out.println( c.getEndDate().isBefore(LocalDate.now()) );
                    throw new ResourceNotFoundException("Con rang buoc boi Hop Dong");
                }
            });
            Customers Customer = getCustomer(id).get();
            customerRepository.delete(Customer);
        }
        catch (Exception ex){
            throw new ResourceNotFoundException(ex.getMessage());
        }
    }

    @Override
    public InfoLogin getLogin(String name) {
        return customerRepository.getLogin(name);
    }

}






