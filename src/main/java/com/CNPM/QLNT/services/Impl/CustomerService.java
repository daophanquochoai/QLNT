package com.CNPM.QLNT.services.Impl;

import com.CNPM.QLNT.exception.ResourceNotFoundException;
import com.CNPM.QLNT.model.*;
import com.CNPM.QLNT.repository.CustomerRepository;
import com.CNPM.QLNT.repository.authRepo;
import com.CNPM.QLNT.response.InfoLogin;
import com.CNPM.QLNT.response.Info_user;
import com.CNPM.QLNT.services.Inter.IContracService;
import com.CNPM.QLNT.services.Inter.ICustomerService;
import com.CNPM.QLNT.services.Inter.IRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService implements ICustomerService {
    private final CustomerRepository customerRepository;
    private final IRoomService iRoomService;
    private final IContracService iContracService;
    @Autowired
    private authRepo auth_repo;
    @Override
    public List<Info_user> getAllCustomer() {
        List<Customers> list = customerRepository.findAll();
        List<Info_user> l = list.stream()
                .filter(c-> c.getUserAuthId().getAuthId().getRole().equals("USER"))
                .map(
                c ->
                {
                    int room;
                    if( c.getRoom() == null){
                        room = 0;
                    }else {
                        room = c.getRoom().getId();
                    }
                    Info_user user = new Info_user(c.getCustomerId(),
                    c.getFirstName(),
                    c.getLastName(),
                    c.getCCCD(),
                    c.getDate_of_birth(),
                    c.getSex(),
                    c.getInfoAddress(),
                    c.getPhoneNumber(),
                    c.getEmail(),
                    room,
                     c.getUserAuthId().getUsersId().getUsername(),
                    c.getUserAuthId().getUsersId().getPassword()
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
    public Customers getAdmin() {
        List<Customers> list = customerRepository.findAll();
        Optional<Customers> adminOptional = list.stream()
                .filter(c -> c.getUserAuthId().getAuthId().getRole().equals("ADMIN"))
                .findFirst();

        return adminOptional.orElse(null); // Trả về null nếu không tìm thấy admin
    }

    @Override
    public List<Info_user> getCustomerByRoomId(int room_id) {
        List<Customers> list = customerRepository.getCustomerByRoomId(room_id);
        List<Info_user> l = list.stream().map(
                c->
                {
                    Info_user user = new Info_user(
                            c.getCustomerId(),
                            c.getFirstName(),
                            c.getLastName(),
                            c.getCCCD(),
                            c.getDate_of_birth(),
                            c.getSex(),
                            c.getInfoAddress(),
                            c.getPhoneNumber(),
                            c.getEmail(),
                            c.getRoom().getId(),
                            c.getUserAuthId().getUsersId().getUsername(),
                            c.getUserAuthId().getUsersId().getPassword()
                    );
                    return user;
                }
        ).collect(Collectors.toList());
        return l;
    }

    @Override
    public Boolean addCustomer(Info_user info) throws Exception {
        List<Info_user> listCus = getAllCustomer();
        boolean check = getAllCustomer().stream().anyMatch(
                c -> ( c.getCCCD().equals(info.getCCCD())|| c.getTaikhoan().equals(info.getTaikhoan()))
        );
        Customers admin = getAdmin();
        if(info.getCCCD().equals(admin.getCCCD()) || (admin.getUserAuthId().getUsersId().getUsername().equals(info.getTaikhoan()))){
            check = true;
        }
        if( check) throw new ResourceNotFoundException("Bi trung CCCD hoac TK_MK");
        List<Info_user> list = getCustomerByRoomId(info.getRoom());
        Customers c = new Customers();
        System.out.println(info.getRoom());
        if( info.getRoom() != 0){
            c.setRoom(iRoomService.getRoom(info.getRoom()).get());
            Room Room = iRoomService.getRoom(info.getRoom()).get();
            if( Room.getLimit() == list.size()){
                return false;
            }
        }

        c.setFirstName(info.getFirst_name());
        c.setLastName(info.getLast_name());
        c.setCCCD(info.getCCCD());
        if(info.getDate_of_birth() != null){
            c.setDate_of_birth(info.getDate_of_birth());
        }
        if(info.getSex() != null){
            c.setSex(info.getSex());
        }
        if(info.getInfo_address() != null){
            c.setInfoAddress(info.getInfo_address());
        }
        if(info.getPhone_number() != null){
            c.setPhoneNumber(info.getPhone_number());
        }
        if(info.getEmail() != null){
            c.setEmail(info.getEmail());
        }

        Users u = new Users();
        u.setUsername(info.getTaikhoan());
        u.setPassword(new BCryptPasswordEncoder().encode(info.getMatkhau()));
        u.setActive(true);
        Auth a =  auth_repo.getAuth();
        UserAuth ua = new UserAuth();
        ua.setUsersId(u);
        ua.setAuthId(a);
        c.setUserAuthId(ua);
        customerRepository.save(c);
        return true;
    }

    @Override
    public void updateCustomer(int id, Info_user info) {
        Customers Customer = getCustomer(id).get();
        boolean check = getAllCustomer().stream().anyMatch(
                c -> (c.getId() != id && ( c.getCCCD().equals(info.getCCCD())|| c.getTaikhoan().equals(info.getTaikhoan())))
        );
        Customers admin = getAdmin();
        if(Customer.getCustomerId() != admin.getCustomerId() && (info.getCCCD().equals(admin.getCCCD()) || (admin.getUserAuthId().getUsersId().getUsername().equals(info.getTaikhoan())))){
            check = true;
        }
        if( check) throw new ResourceNotFoundException("Bi trung CCCD hoac TK_MK");

        if( info.getFirst_name() !=  null){
            Customer.setFirstName(info.getFirst_name());
        }
        if( info.getLast_name() !=  null){
            Customer.setLastName(info.getLast_name());
        }
        if( info.getCCCD() !=  null){
            Customer.setCCCD(info.getCCCD());
        }
        if( info.getInfo_address() !=  null){
            Customer.setInfoAddress(info.getInfo_address());
        }
        if( info.getDate_of_birth() !=  null){
            Customer.setDate_of_birth(info.getDate_of_birth());
        }
        if( info.getSex() !=  null){
            Customer.setSex(info.getSex());
        }
        if( info.getPhone_number() !=  null){
            Customer.setPhoneNumber(info.getPhone_number());
        }
        if( info.getEmail() !=  null){
            Customer.setSex(info.getSex());
        }
        if( info.getTaikhoan() !=  null){
            Customer.getUserAuthId().getUsersId().setUsername(info.getTaikhoan());
        }
        if( info.getMatkhau() !=  null) {
            Customer.getUserAuthId().getUsersId().setPassword(new BCryptPasswordEncoder().encode(info.getMatkhau()));
        }
        if( info.getRoom() != 0 )
            Customer.setRoom(iRoomService.getRoom(info.getRoom()).get());
        customerRepository.save(Customer);
    }

    @Override
    public void deleteCustomer(int id) {
        List<Contracts> listCT = iContracService.getAllContract();
        listCT.stream().forEach( c ->{
            if( c.getCusId().getCustomerId() == id && c.getEndDate().before(new Date()) ){
                throw new ResourceNotFoundException("Con rang buoc boi bill");
            }
        });
        Customers Customer = getCustomer(id).get();
        customerRepository.delete(Customer);
    }

    @Override
    public InfoLogin getLogin(String name) {
        return customerRepository.getLogin(name);
    }

}
