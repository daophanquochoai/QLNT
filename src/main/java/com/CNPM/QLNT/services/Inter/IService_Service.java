package com.CNPM.QLNT.services.Inter;

import com.CNPM.QLNT.model.Services;
import java.util.List;

public interface IService_Service {
    void saveService(Services services);
    void updateService(Integer id,Services services);
    List<Services> getAllService();
}
