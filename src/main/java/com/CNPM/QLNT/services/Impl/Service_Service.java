package com.CNPM.QLNT.services.Impl;

import com.CNPM.QLNT.exception.ResourceNotFoundException;
import com.CNPM.QLNT.model.Services;
import com.CNPM.QLNT.repository.ServiceRepo;
import com.CNPM.QLNT.services.Inter.IService_Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class Service_Service implements IService_Service {
    private final ServiceRepo serviceRepo;

    @Override
    public void saveService(Services services) {
        if( services.getPrice() < 0){
            throw new ResourceNotFoundException("price lon hon 0");
        }
        serviceRepo.save(services);
    }

    @Override
    public void updateService(Integer id,Services services) {
        Optional<Services> s = serviceRepo.findById(id);
        if( s.isEmpty()) throw new ResourceNotFoundException("Khong tim thay service");
        Services ser = s.get();
        if( services.getPrice() >= 0){
            ser.setPrice(services.getPrice());
        }
        ser.setServiceName(services.getServiceName());
        serviceRepo.save(ser);
    }

    @Override
    public List<Services> getAllService() {
        return serviceRepo.findAll();
    }
}
