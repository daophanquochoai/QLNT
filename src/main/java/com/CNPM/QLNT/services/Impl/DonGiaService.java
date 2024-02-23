package com.CNPM.QLNT.services.Impl;

import com.CNPM.QLNT.model.donGia;
import com.CNPM.QLNT.model.requests;
import com.CNPM.QLNT.repository.donGiaRepository;
import com.CNPM.QLNT.services.Inter.IDonGiaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DonGiaService implements IDonGiaService {
    private final donGiaRepository donGiaRepo;

    @Override
    public List<donGia> getDonGia() {
        return donGiaRepo.findAll();
    }

    @Override
    public void saveDonGia(donGia dg) {
        donGiaRepo.save(dg);
    }

    @Override
    public donGia getDonGiaNow() {
        List<donGia> list = getDonGia();
        donGia dg = list.get(list.size() - 1);
        return dg;
    }
}
