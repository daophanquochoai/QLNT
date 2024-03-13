package com.CNPM.QLNT.services.Inter;

import com.CNPM.QLNT.model.PriceQuotation;

import java.util.List;

public interface IDonGiaService {
    List<PriceQuotation> getDonGia();
    void saveDonGia(PriceQuotation dg);
    PriceQuotation getDonGiaNow();
}
