package com.CNPM.QLNT.services.Impl;

import com.CNPM.QLNT.repository.RoomTypeRepo;
import com.CNPM.QLNT.services.Inter.IRoomType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomType implements IRoomType {
    private final RoomTypeRepo roomTypeRepo;
    @Override
    public void addRoomType(com.CNPM.QLNT.model.RoomType roomType) {
        List<com.CNPM.QLNT.model.RoomType> roomTypeList = roomTypeRepo.findAll();

        // Kiểm tra xem roomType đã tồn tại trong danh sách chưa
        boolean isDuplicate = roomTypeList.stream()
                .anyMatch(h -> h.getRoomTypeName().equals(roomType.getRoomTypeName()));

        if (!isDuplicate) {
            roomTypeRepo.save(roomType);
        } else {
            // Nếu roomType đã tồn tại, ném RuntimeException
            throw new RuntimeException("Loại phòng đã tồn tại");
        }
    }

    @Override
    public List<com.CNPM.QLNT.model.RoomType> getAllRoomType() {
        return roomTypeRepo.findAll();
    }
    @Override
    public com.CNPM.QLNT.model.RoomType getRoomTypeByRoomTypeId(int roomTypeId) {
        return roomTypeRepo.getRoomTypeByRoomTypeId(roomTypeId);
    }

}
