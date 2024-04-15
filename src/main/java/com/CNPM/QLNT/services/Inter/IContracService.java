package com.CNPM.QLNT.services.Inter;

import com.CNPM.QLNT.model.Contracts;
import com.CNPM.QLNT.response.InfoContract;

import java.util.List;

public interface IContracService {
    List<Contracts> getAllContract();
    Contracts getContractById(Integer id);
    void saveContract(Integer customerId, Integer roomId, InfoContract infoContract);
}
