package com.CNPM.QLNT.services.Impl;

import com.CNPM.QLNT.exception.ResourceNotFoundException;
import com.CNPM.QLNT.model.ElectricPrice;
import com.CNPM.QLNT.model.WaterPrice;
import com.CNPM.QLNT.repository.ElectricPriceRepo;
import com.CNPM.QLNT.repository.WaterPriceRepo;
import com.CNPM.QLNT.response.PriceQuotation;
import com.CNPM.QLNT.services.Inter.ICustomerService;
import com.CNPM.QLNT.services.Inter.IPriceService;
import com.CNPM.QLNT.services.Inter.IPriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PriceService implements IPriceService {
    private final ElectricPriceRepo electricPriceRepo;
    private final WaterPriceRepo waterPriceRepo;

    @Override
    public List<ElectricPrice> getAllElectricPrice() {
        return electricPriceRepo.findAllByOrderByChangedDateDesc();
    }


    @Override
    public PriceQuotation getPriceNow() {
        List<WaterPrice> w = waterPriceRepo.findAllByOrderByChangedDateDesc();
        List<ElectricPrice> e = electricPriceRepo.findAllByOrderByChangedDateDesc();
        return new PriceQuotation( e.get(e.size()-1).getPrice(), e.get(e.size()-1).getChangedDate(), w.get(w.size()-1).getPrice(), w.get(w.size()-1).getChangedDate() );
    }

    @Override
    public List<WaterPrice> getAllWaterPrice() {
        return waterPriceRepo.findAllByOrderByChangedDateDesc();
    }

    @Override
    public void saveElectricPrice(ElectricPrice e) {
        if( e.getPrice() < 0 ) throw new ResourceNotFoundException("Giá điện phải lớn hơn 0");
        electricPriceRepo.save(e);
    }

    @Override
    public void saveWaterPrice(WaterPrice w) {
        if( w.getPrice() < 0 ) throw new ResourceNotFoundException("Giá nước phải lớn hơn 0");
        waterPriceRepo.save(w);
    }
}
