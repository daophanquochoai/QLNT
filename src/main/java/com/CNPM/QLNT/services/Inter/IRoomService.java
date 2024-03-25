package com.CNPM.QLNT.services.Inter;

import com.CNPM.QLNT.model.Room;
import com.CNPM.QLNT.response.RoomRes;

import java.util.List;
import java.util.Optional;

public interface IRoomService {
    List<RoomRes> allRoom();
    Optional<Room> getRoom(Integer room_id);
    void addRoom(RoomRes roomRes);
    void updateRoom(int id,RoomRes roomRes);
    void deleteRoom(int id) throws Exception;
    List<RoomRes> getAllRoomByStatus(boolean status);
    List<RoomRes> getAllRoomByLimit(int type);
    List<RoomRes> getRoomForBill();
}
