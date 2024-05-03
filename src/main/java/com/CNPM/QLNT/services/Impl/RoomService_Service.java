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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.UnexpectedRollbackException;

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
        return roomServiceRepo.getAllServiceInUseByRoomId(roomId);
    }

    @Override
    public void addRoomService(List<InfoRoomService> infoRoomServiceList) {
        //Kểm tra phòng có dịch vụ đó chưa
        infoRoomServiceList.forEach(infoRoomService -> {
            RoomService check = roomServiceRepo
                    .getRoomServiceByRoomIdAndServiceId(infoRoomService.getRoomId(), infoRoomService.getServiceId());
            //Nếu có tồn tại roomService theo roomId và serviceId thì ktra có còn sử dụng ko bằng endDate
            if (check != null) {
                if (check.getEndDate() == null) {
                    throw new ResourceNotFoundException("Phòng đã đăng kí dịch vụ \"" + check.getService().getServiceName() + "\"");
                }
            }
        });
        infoRoomServiceList.forEach((infoRoomService) -> {
            try {
                RoomService check = roomServiceRepo
                        .getRoomServiceByRoomIdAndServiceId(infoRoomService.getRoomId(), infoRoomService.getServiceId());
                //Kiểm tra roomService theo roomId và serviceId có từng tồn tại chưa
                //Có thì cập nhật lại roomService đó
                if (check != null) {
                    check.setBeginDate(infoRoomService.getBeginDate());
                    check.setEndDate(null);
                    check.setQuantity(infoRoomService.getQuantity());
                    roomServiceRepo.save(check);
                }
                //Không thì tại roomService mới
                else {
                    RoomService roomService = new RoomService();
                    roomService.setBeginDate(infoRoomService.getBeginDate());
                    roomService.setEndDate(null);
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
            } catch (Exception ex) {
                throw new ResourceNotFoundException(ex.getMessage());
            }
        });
    }

    @Override
    public void updateRoomService(Integer roomId, List<InfoRoomService> infoRoomServiceList) {
        roomServiceRepo.getAllRoomServiceInUseByRoomId(roomId).forEach(roomService -> {
            Optional<InfoRoomService> irs = infoRoomServiceList.stream()
                    .filter(infoRoomService -> infoRoomService.getServiceId()==roomService.getService().getServiceId()).findFirst();
            Optional<RoomService> rs = roomServiceRepo.findById(roomService.getRoomServiceId());
            if (irs.isPresent()){
                rs.get().setQuantity(irs.get().getQuantity());
            }else {
                rs.get().setEndDate(LocalDate.now());
            }
            roomServiceRepo.save(rs.get());
        });

    }

    @Override
    public List<RoomService> getServiceOfRoom(Integer roomId) {
        Optional<Room> room = roomRepo.findById(roomId);
        if (room.isEmpty()) throw new ResourceNotFoundException("roomId");
        return room.get().getRoomService();
    }

    @Override
    public List<RoomService> getAllRoomServiceInUse() {
        return roomServiceRepo.getAllRoomServiceInUse();
    }
}
