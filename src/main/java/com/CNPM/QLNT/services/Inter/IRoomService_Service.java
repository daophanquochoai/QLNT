package com.CNPM.QLNT.services.Inter;
import com.CNPM.QLNT.response.InfoRoomService;
import com.CNPM.QLNT.response.InfoService;

import java.time.LocalDate;
import java.util.List;

public interface IRoomService_Service {
    List<InfoService> getServiceByRoomIdMonthYear(Integer roomId);
    void saveRoomService(Integer roomId, InfoRoomService infoRoomService);
    void updateRoomService(Integer id,LocalDate endDate);
}
