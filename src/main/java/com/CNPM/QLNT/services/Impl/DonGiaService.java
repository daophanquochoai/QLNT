package com.CNPM.QLNT.services.Impl;

import com.CNPM.QLNT.model.Customers;
import com.CNPM.QLNT.model.PriceQuotation;
import com.CNPM.QLNT.model.Requests;
import com.CNPM.QLNT.repository.donGiaRepository;
import com.CNPM.QLNT.services.Inter.ICommuService;
import com.CNPM.QLNT.services.Inter.ICustomerService;
import com.CNPM.QLNT.services.Inter.IDonGiaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DonGiaService implements IDonGiaService {
    private final donGiaRepository donGiaRepo;
    private final ICustomerService iCustomerService;
    private final ICommuService iCommuService;

    @Override
    public List<PriceQuotation> getDonGia() {
        return donGiaRepo.findAll();
    }

    @Override
    public void saveDonGia(PriceQuotation dg) {
        dg.setTimeChange(Calendar.getInstance().getTime());
        String mess = "Thay doi : Tien dien = "+ dg.getElectricityPrice() +" , Tien nuoc = "+ dg.getWaterPrice() +" , Thoi gian thay doi = "+ dg.getTimeChange();
        Requests request = new Requests();
        request.setCreatedDatatime(new Date());
        request.setStatus(true);
        request.setMessage(mess);
        Customers chutro = iCustomerService.getAdmin();
        iCommuService.addMessage(request,chutro,null);
        donGiaRepo.save(dg);
    }

    @Override
    public PriceQuotation getDonGiaNow() {
        List<PriceQuotation> list = getDonGia();
        PriceQuotation dg = list.get(list.size() - 1);
        return dg;
    }
}
