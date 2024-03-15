package com.CNPM.QLNT.services.Impl;

import com.CNPM.QLNT.exception.ResourceNotFoundException;
import com.CNPM.QLNT.model.Contracts;
import com.CNPM.QLNT.repository.contractRepo;
import com.CNPM.QLNT.services.Inter.IContracService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ContractService implements IContracService {
    @Autowired
    private final contractRepo contractRP;
    @Override
    public List<Contracts> getAllContract() {
        return contractRP.findAll();
    }

    @Override
    public Contracts getContractById(int id) {
        Optional<Contracts> c = Optional.ofNullable(contractRP.getContractById(id));
        if( c.isEmpty()) throw new ResourceNotFoundException("Not Found");
        return c.get();
    }
}
