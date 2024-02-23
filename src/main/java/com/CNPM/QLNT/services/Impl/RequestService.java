package com.CNPM.QLNT.services.Impl;

import com.CNPM.QLNT.model.requests;
import com.CNPM.QLNT.repository.RequestRepo;
import com.CNPM.QLNT.services.Inter.IRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RequestService implements IRequestService {
    private final RequestRepo requestRepo;

    @Override
    public void addRequest_DonGia(requests re) {
            requestRepo.save(re);
    }
}
