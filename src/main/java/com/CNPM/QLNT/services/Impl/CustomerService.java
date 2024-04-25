package com.CNPM.QLNT.services.Impl;

import com.CNPM.QLNT.exception.ResourceNotFoundException;
import com.CNPM.QLNT.model.*;
import com.CNPM.QLNT.repository.CustomerRepository;
import com.CNPM.QLNT.repository.HistoryCustomerRepo;
import com.CNPM.QLNT.repository.RoomRepo;
import com.CNPM.QLNT.response.InfoLogin;
import com.CNPM.QLNT.response.InfoUser;
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
    public List<InfoUser> getAllCustomer() {

        List<Customer> list = customerRepository.findAll();
        List<InfoUser> l = list.stream()
                .filter(c -> c.getUserAuthId().getRole().equals("USER"))
                .map(
                        c ->
                        {
                            Integer roomId = 0;
                            if (c.getHistoryCustomer() != null) {
                                Optional<HistoryCustomer> h = c.getHistoryCustomer().stream().filter(t -> (t.getEndDate() == null && t.getCustomer().getCustomerId() == c.getCustomerId())).findFirst();
                                if (h.isPresent()) roomId = h.get().getRoomOld().getRoomId();
                            }
                            InfoUser user = new InfoUser(
                                    c.getCustomerId(),
                                    c.getFirstName(),
                                    c.getLastName(),
                                    c.getIdentifier(),
                                    c.getDateOfBirth(),
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
    public Optional<Customer> getCustomer(int cus_id) {
        return Optional.of(customerRepository.findById(cus_id).get());
    }

    @Override
    public List<InfoUser> getCustomerByRoomId(Integer room_id) {
        List<Customer> list = historyCustomerRepo.getCustomersByRoom(room_id);
        List<InfoUser> l = list.stream().map(
                c ->
                {
                    InfoUser user = new InfoUser(
                            c.getCustomerId(),
                            c.getFirstName(),
                            c.getLastName(),
                            c.getIdentifier(),
                            c.getDateOfBirth(),
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
    public Boolean addCustomer(InfoUser info) throws Exception {
        List<InfoUser> listCus = getAllCustomer();
        Customer c = new Customer();
        if (!listCus.isEmpty()) {
            boolean check = getAllCustomer().stream().anyMatch(
                    cus -> (cus.getIdentifier().equals(info.getIdentifier()) || cus.getUsername().equals(info.getUsername()))
            );
            if (check) throw new ResourceNotFoundException("Bi trung Identify hoac Tai khoan");
        }
        List<InfoUser> list = getCustomerByRoomId(info.getRoomId());
        if (info.getRoomId() != 0 && !iRoomService.getAllRoomByStatus(true).isEmpty()) {
            Room Room = iRoomService.getRoom(info.getRoomId()).get();
            if (Room.getLimit() == list.size()) {
                return false;
            }
        }

        c.setFirstName(info.getFirstName());
        c.setLastName(info.getLastName());
        if (info.getIdentifier() == null || info.getIdentifier().length() != 12) {
            throw new ResourceNotFoundException("Identify");
        }
        c.setIdentifier(info.getIdentifier());
        if (info.getDateOfBirth() != null) {
            if (info.getDateOfBirth().isAfter(LocalDate.now())) throw new ResourceNotFoundException("dateOfBirth");
            c.setDateOfBirth(info.getDateOfBirth());
        }
        if (info.getSex() != null) {
            c.setSex(info.getSex());
        }
        if (info.getInfoAddress() != null) {
            c.setInfoAddress(info.getInfoAddress());
        }
        if (info.getPhoneNumber() != null) {
            if (info.getPhoneNumber().length() != 10 || info.getPhoneNumber().charAt(0) != '0')
                throw new ResourceNotFoundException("phoneNumber");
            c.setPhoneNumber(info.getPhoneNumber());
        }
        if (info.getEmail() != null) {
            Matcher matcher = pattern.matcher(info.getEmail());
            if (!matcher.matches()) throw new ResourceNotFoundException("email");
            c.setEmail(info.getEmail());
        }
        HistoryCustomer historyCustomer = new HistoryCustomer();
        if (info.getRoomId() != 0) {
            if (roomRepo.findById(info.getRoomId()).isEmpty()) throw new ResourceNotFoundException("room");
            else {
                Room r = roomRepo.findById(info.getRoomId()).get();
                if (r.getLimit() < historyCustomerRepo.getCustomersByRoom(r.getRoomId()).size())
                    throw new ResourceNotFoundException("room day");
                historyCustomer.setRoomOld(r);
                historyCustomer.setBeginDate(LocalDate.now());
            }
        }

        UserAuth ua = new UserAuth();
        ua.setUsername(info.getUsername());
        ua.setPassword(new BCryptPasswordEncoder().encode(info.getPassword()));
        ua.setActive(true);
        ua.setRole("USER");
        c.setUserAuthId(ua);
        Customer customer = customerRepository.save(c);
        if (info.getRoomId() != 0) {
            historyCustomer.setCustomer(customer);
            historyCustomerRepo.save(historyCustomer);
        }
        return true;
    }

    @Override
    public void updateCustomer(int id, InfoUser info) {
        Optional<Customer> C = getCustomer(id);
        if (C.isEmpty()) throw new ResourceNotFoundException("Khong tim thay customer");
        Customer customer = C.get();

        int room = 0;
        if (customer.getHistoryCustomer() != null) {
            room = customer.getHistoryCustomer().stream().filter(t -> t.getEndDate() == null).findFirst().get().getRoomOld().getRoomId();
        }
        Optional<Contract> contract = iContracService.getContractByRoomid(room);
        if (contract.isPresent()) {
            if (contract.get().getCustomer().getCustomerId() == customer.getCustomerId()) {
                throw new ResourceNotFoundException("Khach hang la chu phong");
            }
        }
        boolean check = getAllCustomer().stream().anyMatch(
                c -> (c.getCustomerId() != id && (c.getIdentifier().equals(info.getIdentifier()) || c.getUsername().equals(info.getUsername())))
        );
        if (check) throw new ResourceNotFoundException("Bi trung CCCD hoac TK_MK");
        if (info.getFirstName() != null) {
            customer.setFirstName(info.getFirstName());
        }
        if (info.getLastName() != null) {
            customer.setLastName(info.getLastName());
        }
        if (info.getIdentifier() != null) {
            if (info.getIdentifier().length() != 12) {
                throw new ResourceNotFoundException("Identify");
            }
            customer.setIdentifier(info.getIdentifier());
        }
        if (info.getInfoAddress() != null) {
            customer.setInfoAddress(info.getInfoAddress());
        }
        if (info.getDateOfBirth() != null) {
            if (info.getDateOfBirth().isAfter(LocalDate.now())) throw new ResourceNotFoundException("dateOfBirth");
            customer.setDateOfBirth(info.getDateOfBirth());
        }
        if (info.getSex() != null) {
            customer.setSex(info.getSex());
        }
        if (info.getPhoneNumber() != null) {
            if (info.getPhoneNumber().length() != 10 || info.getPhoneNumber().charAt(0) != '0')
                throw new ResourceNotFoundException("phoneNumber");
            customer.setPhoneNumber(info.getPhoneNumber());
        }
        if (info.getEmail() != null) {
            Matcher matcher = pattern.matcher(info.getEmail());
            if (!matcher.matches()) throw new ResourceNotFoundException("email");
            customer.setEmail(info.getEmail());
        }
        if (info.getUsername() != null) {
            customer.getUserAuthId().setUsername(info.getUsername());
        }
        if (info.getPassword() != null) {
            customer.getUserAuthId().setPassword(new BCryptPasswordEncoder().encode(info.getPassword()));
        }
        if (info.getRoomId() != 0 && info.getRoomId() != room) {
            Optional<HistoryCustomer> h = customer.getHistoryCustomer().stream().filter(t -> t.getEndDate() == null).findFirst();
            if (roomRepo.findById(info.getRoomId()).isEmpty())
                throw new ResourceNotFoundException("Khong tim thay phong");
            if (h.isPresent()) {
                h.get().setEndDate(LocalDate.now());
                h.get().setRoomNew(roomRepo.findById(info.getRoomId()).get());
            }
            HistoryCustomer historyCustomer = new HistoryCustomer();
            historyCustomer.setBeginDate(LocalDate.now());
            historyCustomer.setRoomOld(roomRepo.findById(info.getRoomId()).get());
            historyCustomer.setCustomer(customer);
            historyCustomerRepo.save(historyCustomer);
        }
        if (info.getRoomId() == 0) {
            Optional<HistoryCustomer> h = customer.getHistoryCustomer().stream().filter(t -> t.getEndDate() == null).findFirst();
            h.ifPresent(historyCustomer -> historyCustomer.setEndDate(LocalDate.now()));
        }
        customerRepository.save(customer);
    }

    @Override
    public void deleteCustomer(int id) {
        try {
            List<Contract> listCT = iContracService.getAllContract();
            listCT.stream().forEach(c -> {
                if (c.getCustomer().getCustomerId() == id && (c.getEndDate().isAfter(LocalDate.now()) || !c.getStatus())) {
                    System.out.println(c.getEndDate().isBefore(LocalDate.now()));
                    throw new ResourceNotFoundException("Con rang buoc boi Hop Dong");
                }
            });
            Customer Customer = getCustomer(id).get();
            customerRepository.delete(Customer);
        } catch (Exception ex) {
            throw new ResourceNotFoundException(ex.getMessage());
        }
    }

    @Override
    public InfoLogin getLogin(String name) {
        return customerRepository.getLogin(name);
    }

}






