package com.CNPM.QLNT.services.Inter;

import com.CNPM.QLNT.model.Service;
import java.util.List;

public interface IService_Service {
    void saveService(Service service);
    void updateService(Integer id, Service service);
    List<Service> getAllService();
}
