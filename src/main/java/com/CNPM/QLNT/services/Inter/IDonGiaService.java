package com.CNPM.QLNT.services.Inter;


import com.CNPM.QLNT.model.ElectricPrice;
import com.CNPM.QLNT.model.WaterPrice;
import com.CNPM.QLNT.response.PriceQuotation;

import java.util.List;

public interface IDonGiaService {
    List<ElectricPrice> getAllElectricProce();
    List<WaterPrice> getAllWaterPrice();
    void saveDElecPrice(ElectricPrice e);
    void saveWaterPrice(WaterPrice w);
    PriceQuotation getDonGiaNow();
}
