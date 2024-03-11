package com.CNPM.QLNT.services.Impl;

import com.CNPM.QLNT.model.users;
import com.CNPM.QLNT.repository.userRepo;
import com.CNPM.QLNT.services.Inter.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    @Autowired
    private final userRepo UserRepo;
    @Override
    public List<users> getAllUser() {
        return UserRepo.findAll();
    }
}
