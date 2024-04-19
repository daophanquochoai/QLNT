package com.CNPM.QLNT.services.Impl;

import com.CNPM.QLNT.exception.ResourceNotFoundException;
import com.CNPM.QLNT.model.Request;
import com.CNPM.QLNT.repository.RequestRepo;
import com.CNPM.QLNT.services.Inter.IRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RequestService implements IRequestService {
    private final RequestRepo requestRepo;

    @Override
    public void addRequest_DonGia(Request re) {
            requestRepo.save(re);
    }

    @Override
    public List<Request> getAllRequestOfCustomerByStatus(boolean status) {
        return requestRepo.getRequestOfCustomerByStatus(status);
    }

    @Override
    public List<Request> getAllRequestOfCustomer() {
        return requestRepo.getAllRequestOfCustomer();
    }

    @Override
    public void updateRequest(int id) {
        Optional<Request> r = requestRepo.findById(id);
        if( r.isEmpty()) throw new ResourceNotFoundException("Khong Tim Thay Yeu Cau");
        r.get().setStatus(true);
        requestRepo.save(r.get());
    }

    @Override
    public void deleteCommunication(int id) {
        Optional<Request> r = requestRepo.findById(id);
        if( r.isEmpty()) throw new ResourceNotFoundException("Khong Tim Thay Yeu Cau");
        if( r.get().getStatus() == false) throw new ResourceNotFoundException("Vui long dap ung yeu cay truoc");
        requestRepo.delete(r.get());
    }

    @Override
    public List<Request> getNoticeBySender(Integer id) {
        return requestRepo.getAllRequestOfAdmin(id);
    }
}
