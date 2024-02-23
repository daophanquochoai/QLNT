package com.CNPM.QLNT.services.Inter;

import com.CNPM.QLNT.model.donGia;

import java.util.List;

public interface IDonGiaService {
    List<donGia> getDonGia();
    void saveDonGia(donGia dg);
    donGia getDonGiaNow();
}
