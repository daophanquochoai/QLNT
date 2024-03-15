package com.CNPM.QLNT.services.Inter;

import com.CNPM.QLNT.model.Contracts;

import java.util.List;

public interface IContracService {
    List<Contracts> getAllContract();
    Contracts getContractById(int id);
}
