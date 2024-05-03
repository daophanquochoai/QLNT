package com.CNPM.QLNT.services.Impl;

import com.CNPM.QLNT.exception.ResourceNotFoundException;
import com.CNPM.QLNT.model.Room;
import com.CNPM.QLNT.model.RoomService;
import com.CNPM.QLNT.model.Service;
import com.CNPM.QLNT.repository.RoomRepo;
import com.CNPM.QLNT.repository.RoomServiceRepo;
import com.CNPM.QLNT.repository.ServiceRepo;
import com.CNPM.QLNT.response.InfoRoomService;
import com.CNPM.QLNT.response.InfoService;
import com.CNPM.QLNT.services.Inter.IRoomService_Service;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.time.LocalDate;
import java.util.List;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class RoomService_Service implements IRoomService_Service {
    private final RoomServiceRepo roomServiceRepo;
    private final RoomRepo roomRepo;
    private final ServiceRepo serviceRepo;

    @Override
    public List<InfoService> getServiceByRoomId(Integer roomId) {
        LocalDate currentDate = LocalDate.now();
        return roomServiceRepo.getAllServiceByRoomIdMonthYear(roomId, currentDate.getMonthValue(), currentDate.getYear());
    }

    @Override
    public void saveRoomService(Integer roomId, InfoRoomService infoRoomService) {
        RoomService roomService = new RoomService();
        if (infoRoomService.getBeginDate() != null) {
            roomService.setBeginDate(infoRoomService.getBeginDate());
        } else throw new ResourceNotFoundException("Ngày bắt đầu không hợp lệ");
        if (infoRoomService.getEndDate() != null) {
            if (infoRoomService.getEndDate().isAfter(infoRoomService.getBeginDate())) {
                roomService.setEndDate(infoRoomService.getEndDate());
            } else throw new ResourceNotFoundException("Ngày kết thúc không hợp lệ");
        }
        if (infoRoomService.getQuantity() < 0) throw new ResourceNotFoundException("Số lượng phải lớn hơn 0");
        roomService.setQuantity(infoRoomService.getQuantity());
        if (serviceRepo.findById(infoRoomService.getServiceId()).isEmpty())
            throw new ResourceNotFoundException("Không tìm thấy dịch vụ");
        if (roomRepo.findById(infoRoomService.getRoomId()).isEmpty())
            throw new ResourceNotFoundException("Không tìm thấy phòng");
        Room room = roomRepo.findById(infoRoomService.getRoomId()).get();
        Service service = serviceRepo.findById(infoRoomService.getServiceId()).get();
        roomService.setService(service);
        roomService.setRoom(room);
        roomServiceRepo.save(roomService);
    }

    @Override
    public void updateRoomService(Integer roomServiceId, LocalDate endDate) {
        Optional<RoomService> r = roomServiceRepo.findById(roomServiceId);
        if (r.isEmpty()) throw new ResourceNotFoundException("Không tìm thấy dịch vụ phòng");
        if (r.get().getBeginDate().isAfter(endDate)) throw new ResourceNotFoundException("Ngày kết thúc không hợp lệ");
        r.get().setEndDate(endDate);
        roomServiceRepo.save(r.get());
    }

    @Override
    public List<RoomService> getServiceOfRoom(Integer roomId) {
        Optional<Room> room =  roomRepo.findById(roomId);
        if( room.isEmpty()) throw new ResourceNotFoundException("roomId");
        return room.get().getRoomService();
    }

    @Override
    public List<RoomService> getAllRoomServiceInUse(){
        return roomServiceRepo.getAllRoomServiceInUse();
    }
}
