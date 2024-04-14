package com.CNPM.QLNT.services.Impl;

import com.CNPM.QLNT.model.ElectricPrice;
import com.CNPM.QLNT.model.WaterPrice;
import com.CNPM.QLNT.repository.ElectricPriceRepo;
import com.CNPM.QLNT.repository.WaterPriceRepo;
import com.CNPM.QLNT.response.PriceQuotation;
import com.CNPM.QLNT.services.Inter.ICustomerService;
import com.CNPM.QLNT.services.Inter.IDonGiaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DonGiaService implements IDonGiaService {
    private final ElectricPriceRepo electricPriceRepo;
    private final WaterPriceRepo waterPriceRepo;
    private final ICustomerService iCustomerService;

    @Override
    public List<ElectricPrice> getAllElectricProce() {
        return electricPriceRepo.findAllByOrderByDataChangedDesc();
    }


    @Override
    public PriceQuotation getDonGiaNow() {
        List<WaterPrice> w = waterPriceRepo.findAllByOrderByDataChangedDesc();
        List<ElectricPrice> e = electricPriceRepo.findAllByOrderByDataChangedDesc();
        return new PriceQuotation( e.get(e.size()-1).getPrice(), e.get(e.size()-1).getDataChanged(), w.get(w.size()-1).getPrice(), w.get(w.size()-1).getDataChanged() );
    }

    @Override
    public List<WaterPrice> getAllWaterPrice() {
        return waterPriceRepo.findAllByOrderByDataChangedDesc();
    }

    @Override
    public void saveDElecPrice(ElectricPrice e) {
        electricPriceRepo.save(e);
    }

    @Override
    public void saveWaterPrice(WaterPrice w) {
        waterPriceRepo.save(w);
    }
}
