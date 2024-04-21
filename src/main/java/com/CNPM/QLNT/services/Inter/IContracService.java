package com.CNPM.QLNT.services.Inter;

import com.CNPM.QLNT.model.Contract;
import com.CNPM.QLNT.response.InfoContract;

import java.util.List;
import java.util.Optional;

public interface IContracService {
    List<Contract> getAllContract();
    Contract getContractById(Integer id);
    void saveContract(Integer customerId, Integer roomId, InfoContract infoContract);
    Optional<Contract> getContractByRoomid( Integer roomId);
}
