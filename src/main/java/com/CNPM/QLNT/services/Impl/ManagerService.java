package com.CNPM.QLNT.services.Impl;

import com.CNPM.QLNT.exception.ResourceNotFoundException;
import com.CNPM.QLNT.model.Manager;
import com.CNPM.QLNT.model.UserAuth;
import com.CNPM.QLNT.repository.CustomerRepository;
import com.CNPM.QLNT.repository.ManagerRepo;
import com.CNPM.QLNT.repository.UserAuthRepo;
import com.CNPM.QLNT.response.InfoOfManager;
import com.CNPM.QLNT.services.Inter.IManagerService;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import java.util.Optional;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ManagerService implements IManagerService {
    private final ManagerRepo managerRepo;
    private final UserAuthRepo userAuthRepo;
    private final CustomerRepository customerRepo;
    // format email va regex
    private final String Email_Regex = "^\\\\w+([-+.']\\\\w+)*@\\\\w+([-.]\\\\w+)*\\\\.\\\\w+([-.]\\\\w+)*$";
    private final Pattern pattern = Pattern.compile(Email_Regex);
    @Override
    public List<Manager> getAllManager() {
        return managerRepo.findAll();
    }

    @Override
    public Optional<Manager> getManagerById(Integer id) {
        Optional<Manager> m = managerRepo.findById(id);
        if( m.isEmpty() ) throw new ResourceNotFoundException("Manager không tìm thấy");
        return m;
    }

    @Override
    public void deleteManagerById(Integer id) {
        Optional<Manager> m = managerRepo.findById(id);
        if( m.isEmpty() ) throw new ResourceNotFoundException("Manager không tìm thấy");
        managerRepo.delete(m.get());
    }

    @Override
    public void updateManager(Integer id, InfoOfManager info) {
        Optional<Manager> m = managerRepo.findById(id);
        if( m.isEmpty() ) throw new ResourceNotFoundException("Manager không tìm thấy");
        if( info.getFirstName() != null){
            m.get().setFirstName( info.getFirstName() );
        }
        if( info.getLastName() != null){
            m.get().setLastName( info.getLastName() );
        }
        if( info.getSex() != null ){
            m.get().setSex( info.getSex());
        }
        if( info.getInfoAddress() != null ){
            m.get().setInfoAddress(info.getInfoAddress());
        }
        if( info.getCCCD() != null ){
            if( info.getCCCD().length() == 12){
                if( !info.getCCCD().equals(m.get().getCCCD())){
                    if( managerRepo.findByCCCD(info.getCCCD()).isEmpty() ){
                        if( customerRepo.findByCCCD(info.getCCCD()).isEmpty()){
                            m.get().setCCCD(info.getCCCD());
                        }else throw new ResourceNotFoundException("CCCD trùng với khách hàng");
                    }else throw new ResourceNotFoundException("CCCD trùng");
                }
            }else{
                throw new ResourceNotFoundException("Căn cước công dân phải có chiều dài 12");
            }
        }
        if( info.getDate_of_birth() != null ){
            if( ((LocalDate) info.getDate_of_birth()).isBefore(LocalDate.now()) ){
                m.get().setDate_of_birth(info.getDate_of_birth());
            }else{
                throw new ResourceNotFoundException("Ngay sinh nằm trước hiện tại");
            }
        }
        if( info.getPhoneNumber() != null ){
            if( info.getPhoneNumber().charAt(0) == '0' && info.getPhoneNumber().length() == 10){
                m.get().setPhoneNumber( info.getPhoneNumber());
            }else{
                throw new ResourceNotFoundException("Không đúng format sdt");
            }
        }
        if( info.getEmail() != null ){
            Matcher matcher = pattern.matcher(info.getEmail());
            if( matcher.matches() ){
                m.get().setEmail(info.getEmail());
            }else{
                throw new ResourceNotFoundException("Không đúng format email");
            }
        }
        if( info.getUsername() != null){
            if( m.get().getUserAuthId().getUsername() != info.getUsername()){
                Optional<UserAuth> u = userAuthRepo.findByUsername(info.getUsername());
                if( u.isPresent() ){
                    throw new ResourceNotFoundException("Trùng username");
                }else{
                    m.get().getUserAuthId().setUsername(info.getUsername());
                }
            }
        }
        if ( info.getPassword() != null ){
            m.get().getUserAuthId().setPassword(info.getPassword());
        }
        if(info.getActive() != null){
            m.get().getUserAuthId().setActive(info.getActive());
        }
        managerRepo.save(m.get());
    }
}
