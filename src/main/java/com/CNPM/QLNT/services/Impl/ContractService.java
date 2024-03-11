package com.CNPM.QLNT.services.Impl;

import com.CNPM.QLNT.model.contracts;
import com.CNPM.QLNT.repository.contractRepo;
import com.CNPM.QLNT.services.Inter.IContracService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContractService implements IContracService {
    @Autowired
    private final contractRepo contractRP;
    @Override
    public List<contracts> getAllContract() {
        return contractRP.findAll();
    }
}
