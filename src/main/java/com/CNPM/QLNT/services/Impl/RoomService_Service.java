package com.CNPM.QLNT.services.Impl;

import com.CNPM.QLNT.exception.ResourceNotFoundException;
import com.CNPM.QLNT.model.Room;
import com.CNPM.QLNT.model.RoomService;
import com.CNPM.QLNT.model.Services;
import com.CNPM.QLNT.repository.RoomRepo;
import com.CNPM.QLNT.repository.RoomServiceRepo;
import com.CNPM.QLNT.repository.ServiceRepo;
import com.CNPM.QLNT.response.InfoRoomService;
import com.CNPM.QLNT.response.InfoService;
import com.CNPM.QLNT.services.Inter.IRoomService_Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.time.LocalDate;
import java.util.List;
@Service
@RequiredArgsConstructor
public class RoomService_Service implements IRoomService_Service {
    private final RoomServiceRepo roomServiceRepo;
    private final RoomRepo roomRepo;
    private final ServiceRepo serviceRepo;
    @Override
    public List<InfoService> getServiceByRoomIdAndDate(Integer roomId, LocalDate date) {
        return roomServiceRepo.getAllServiceByRoomId(roomId,date);
    }

    @Override
    public void saveRoomService(Integer roomId, InfoRoomService infoRoomService) {
        RoomService roomService = new RoomService();
        if( infoRoomService.getBeginDate() != null ){
            roomService.setBeginDate(infoRoomService.getBeginDate());
        }else throw new ResourceNotFoundException("BeginDAte khác null");
        if( infoRoomService.getEndDate() != null ){
            if( infoRoomService.getEndDate().isAfter(infoRoomService.getBeginDate())){
                roomService.setEndDate(infoRoomService.getEndDate());
            }else throw new ResourceNotFoundException("EndDate nằm sau BeginDate");
        }
        if( infoRoomService.getQuantity() < 0 ) throw new ResourceNotFoundException("Quantity lon hon 0");
        roomService.setQuantity(infoRoomService.getQuantity());
        if( serviceRepo.findById(infoRoomService.getServices()).isEmpty()) throw new ResourceNotFoundException("Khong tim thay service");
        if( roomRepo.findById(infoRoomService.getRoomId()).isEmpty()) throw new ResourceNotFoundException("Khong tim thay room");
        Room room = roomRepo.findById(infoRoomService.getRoomId()).get();
        Services services = serviceRepo.findById(infoRoomService.getServices()).get();
        roomService.setServiceId(services);
        roomService.setRoomId(room);
        roomServiceRepo.save(roomService);
    }

    @Override
    public void updateRoomService(Integer id, LocalDate endDate) {
        Optional<RoomService> r = roomServiceRepo.findById(id);
        if( r.isEmpty()) throw new ResourceNotFoundException("Khong tim thay roomService");
        if( r.get().getBeginDate().isAfter(endDate)) throw new ResourceNotFoundException("endDate");
        r.get().setEndDate(endDate);
        roomServiceRepo.save(r.get());
    }
}
