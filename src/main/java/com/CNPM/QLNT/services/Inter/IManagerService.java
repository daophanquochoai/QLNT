package com.CNPM.QLNT.services.Inter;

import com.CNPM.QLNT.model.Manager;
import com.CNPM.QLNT.response.InfoOfManager;

import java.util.Optional;
import java.util.List;

public interface IManagerService {
    List<Manager> getAllManager();
    Optional<Manager> getManagerById(Integer id);
    void deleteManagerById(Integer id);
    void updateManager( Integer id, InfoOfManager info);
}
