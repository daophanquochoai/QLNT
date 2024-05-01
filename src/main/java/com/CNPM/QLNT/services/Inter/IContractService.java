package com.CNPM.QLNT.services.Inter;

import com.CNPM.QLNT.model.Contract;
import com.CNPM.QLNT.response.InfoContract;

import java.util.List;
import java.util.Optional;

public interface IContractService {
    List<Contract> getAllContract();
    Contract getContractByCustomerId(Integer customerId);
    void saveContract(Integer customerId, Integer roomId, InfoContract infoContract);
    Optional<Contract> getContractByRoomId( Integer roomId);
}
