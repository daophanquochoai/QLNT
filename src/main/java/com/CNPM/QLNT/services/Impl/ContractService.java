package com.CNPM.QLNT.services.Impl;

import com.CNPM.QLNT.exception.ResourceNotFoundException;
import com.CNPM.QLNT.model.Contracts;
import com.CNPM.QLNT.repository.ContractRepo;
import com.CNPM.QLNT.services.Inter.IContracService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ContractService implements IContracService {
    @Autowired
    private final ContractRepo contractRP;
    @Override
    public List<Contracts> getAllContract() {
        return contractRP.findAll();
    }

    @Override
    public Contracts getContractById(Integer id) {
        log.info("{}",contractRP.getContractById(id));
        Optional<Contracts> c = Optional.ofNullable(contractRP.getContractById(id));
        if( c.isEmpty()) throw new ResourceNotFoundException("Not Found");
        return c.get();
    }
}
