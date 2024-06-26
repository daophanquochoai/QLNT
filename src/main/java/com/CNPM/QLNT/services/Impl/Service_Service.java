package com.CNPM.QLNT.services.Impl;

import com.CNPM.QLNT.exception.ResourceNotFoundException;
import com.CNPM.QLNT.model.Bill;
import com.CNPM.QLNT.model.Service;
import com.CNPM.QLNT.repository.ServiceRepo;
import com.CNPM.QLNT.services.Inter.IService_Service;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import java.util.List;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class Service_Service implements IService_Service {
    private final ServiceRepo serviceRepo;

    @Override
    public void saveService(Service service) {
        if( service.getPrice() < 0){
            throw new ResourceNotFoundException("Giá phải lớn hơn 0");
        }
        serviceRepo.save(service);
    }

    @Override
    public void updateService(Integer serviceId, Service service) {
        Optional<Service> s = serviceRepo.findById(serviceId);
        if( s.isEmpty()) throw new ResourceNotFoundException("Không tìm thấy dịch vụ");
        Service ser = s.get();
        if( service.getPrice() >= 0){
            ser.setPrice(service.getPrice());
        }
        ser.setServiceName(service.getServiceName());
        serviceRepo.save(ser);
    }
    @Override
    public void deleteService(Integer serviceId) {
        Optional<Service> service = serviceRepo.findById(serviceId);
        if(service.isEmpty()) throw new ResourceNotFoundException("Không tìm thấy dịch vụ");
        serviceRepo.delete(service.get());
    }

    @Override
    public List<Service> getAllService() {
        return serviceRepo.findAll();
    }
}
